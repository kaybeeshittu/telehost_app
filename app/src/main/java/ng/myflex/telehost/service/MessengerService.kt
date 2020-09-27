package ng.myflex.telehost.service

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.gson.Gson
import com.romellfudi.ussdlibrary.USSDService
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.common.Properties
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.payload.MessagePayload
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.service.controller.SmsServiceController
import ng.myflex.telehost.service.controller.UssdServiceController
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import ng.myflex.telehost.util.md5
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import java.lang.Runnable
import java.util.*
import javax.inject.Inject

class MessengerService : USSDService(), LifecycleOwner {
    companion object {
        private val tag = MessengerService::class.java.name

        private const val defaultWaitTime = 1000L
    }

    @Inject
    internal lateinit var gson: Gson

    @Inject
    internal lateinit var properties: Properties

    @Inject
    internal lateinit var dispatcher: Dispatcher

    @Inject
    internal lateinit var sessionStorageService: SessionStorageService

    @Inject
    internal lateinit var principalService: PrincipalService

    @Inject
    internal lateinit var notificationService: NotificationService

    @Inject
    internal lateinit var smsServiceController: SmsServiceController

    @Inject
    internal lateinit var ussdServiceController: UssdServiceController

    @Inject
    internal lateinit var activityRepository: ActivityRepository

    private lateinit var token: String

    private lateinit var destinationKey: String

    private lateinit var disposables: CompositeDisposable

    private lateinit var disposableMap: MutableMap<Long, Disposable>

    private lateinit var stompClient: StompClient

    private lateinit var lifeCycleRegistry: LifecycleRegistry

    @Volatile
    private var isRetry: Boolean = false

    private var activated: Boolean = false

    private var retries: Int = 0

    private var waitTime: Long = defaultWaitTime

    private var topicDisposable: Disposable? = null

    private val sender: String = "/topic/messenger"

    private val topic: String by lazy { "/topic/notification/${destinationKey.md5()}" }

    private val url: String by lazy {
        "${properties.getWebsocketUrl()}messenger/websocket?access_token=$token"
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        disposableMap = mutableMapOf()
        disposables = CompositeDisposable()

        token = properties.getNotificationKey()
        destinationKey = sessionStorageService.getString(Constant.fcmSession) ?: ""

        lifeCycleRegistry = LifecycleRegistry(this)
        lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
    }

    @ExperimentalStdlibApi
    override fun onServiceConnected() {
        super.onServiceConnected()
        lifeCycleRegistry.currentState = Lifecycle.State.RESUMED

        var isAuthenticated = false
        principalService.getStream().observe(this, androidx.lifecycle.Observer {
            isAuthenticated = true
            onAuthenticate(it)
        })
        GlobalScope.launch(dispatcher.IO) {
            when (val response = execute { principalService.identity() }) {
                is ServiceResponse.OnSuccess -> {
                    if (!isAuthenticated) {
                        onAuthenticate(response.data)
                    }
                }
            }
        }
    }

    @ExperimentalStdlibApi
    private fun onAuthenticate(account: Account?) {
        if (account == null) {
            closeConnection()
        } else {
            notificationService.initialize(account)
            establishConnection()
        }
    }

    @ExperimentalStdlibApi
    private fun establishConnection() {
        clear()
        activated = true
        
        disposables.add(stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { onResponse(it) }, { onError(it) }
            ))
        stompClient.connect()
    }

    private fun subscribe() {
        topicDisposable?.dispose()
        topicDisposable = stompClient.topic(topic)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { onMessage(it) }, { onError(it) }
            )
    }

    private fun onResponse(event: LifecycleEvent) {
        when (event.type) {
            LifecycleEvent.Type.OPENED -> onConnect()
            LifecycleEvent.Type.CLOSED -> onDisconnect()
            LifecycleEvent.Type.ERROR -> onConnectionError(event.exception)
            else -> {
            }
        }
    }

    private fun onMessage(stompMessage: StompMessage) {
        val model = gson.fromJson(stompMessage.payload, MessagePayload::class.java)
        disposableMap[model.id] = stompClient.send(sender, stompMessage.payload)
            .unsubscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                onMessageReceived(model)
                disposableMap[model.id]?.dispose()
                disposableMap.remove(model.id)
            }, {
                onError(it)
                disposableMap[model.id]?.dispose()
                disposableMap.remove(model.id)
            })
    }

    private fun onMessageReceived(payload: MessagePayload) = GlobalScope.launch(dispatcher.IO) {
        if (payload.type.toLowerCase(Locale.getDefault()) == "ussd") {
            ussdServiceController.dialUssd(payload)
        } else if (payload.type.toLowerCase(Locale.getDefault()) == "sms") {
            smsServiceController.sendMessage(payload)
        }
    }

    private fun onConnect() {
        Log.d(tag, "Connection opened!")

        reset()
        subscribe()

        notificationService.setIsConnected(true)
    }

    private fun onError(exception: Throwable) {
        Log.e(tag, "${exception.message}")
    }

    private fun onConnectionError(exception: Throwable?) {
        Log.e(tag, "${exception?.message}")
    }

    private fun onDisconnect() {
        Log.d(tag, "Connection closed!")

        notificationService.setIsConnected(false)
        rescheduleConnection()
    }

    private fun rescheduleConnection() = runBlocking(dispatcher.IO) {
        isRetry = true
        delay(waitTime)

        stompClient.connect()

        isRetry = false
        retries += 1
        waitTime += (Random()).nextInt(100)
    }

    private fun reset() {
        retries = 0
        waitTime = defaultWaitTime
    }

    private fun clear(reset: Boolean = true) {
        disposableMap.values.forEach { disposables.add(it) }

        disposables.dispose()
        topicDisposable?.dispose()
        disposableMap.clear()
        if (reset) {
            disposables = CompositeDisposable()
        }
    }

    override fun getLifecycle(): Lifecycle = lifeCycleRegistry

    private fun closeConnection() {
        clear()
        activated = false
        notificationService.close()

        stompClient.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifeCycleRegistry.currentState = Lifecycle.State.DESTROYED

        closeConnection()
    }
}