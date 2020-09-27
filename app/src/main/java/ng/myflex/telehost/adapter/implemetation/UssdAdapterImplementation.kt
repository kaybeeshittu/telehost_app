package ng.myflex.telehost.adapter.implemetation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.romellfudi.ussdlibrary.USSDApi
import com.romellfudi.ussdlibrary.USSDController
import ng.myflex.telehost.adapter.UssdAdapter
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Activity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class UssdAdapterImplementation @Inject constructor(
    private val context: Context
) : UssdAdapter, USSDController.CallbackInvoke {
    private val ussdController: USSDApi by lazy {
        USSDController.getInstance(context)
    }

    private var map: HashMap<String, HashSet<String>> = HashMap<String, HashSet<String>>()

    private lateinit var activity: Activity

    private var active = false
    private var params = mutableListOf<String>()

    init {
        map["KEY_LOGIN"] = HashSet(listOf("espere", "waiting", "loading", "esperando"))
        map["KEY_ERROR"] = HashSet(listOf("problema", "problem", "error", "null"))
    }

    @ExperimentalStdlibApi
    @Synchronized
    override suspend fun dialUssd(activity: Activity) = suspendCoroutine<String?> {
        val filter = IntentFilter()
        val action = "${UssdAdapterImplementation::class.java.name}[${activity.id}]"

        this.activity = activity
        this.params = if (activity.param != null) {
            activity.param.toMutableList()
        } else mutableListOf()

        filter.addAction(action)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val message = intent?.getStringExtra(Constant.dialResponse)

                it.resume(message)
                this@UssdAdapterImplementation.context.unregisterReceiver(this)
            }
        }, filter)
        dial()
    }

    private fun dial() {
        val sim = if (activity.simPort <= 0) 0 else activity.simPort - 1
        val dial = activity.number

        this.active = true
        ussdController.callUSSDInvoke(dial, sim, map, this)
    }

    private fun respond(context: Context, dialer: Long, message: String) {
        val intent = Intent()

        active = false

        intent.putExtra(Constant.dialResponse, message)
        intent.action = "${UssdAdapterImplementation::class.java.name}[$dialer]"

        context.sendBroadcast(intent)
    }

    @ExperimentalStdlibApi
    override fun responseInvoke(message: String) {
        if (params.isNullOrEmpty() || !active) {
            ussdController.cancel()
            over(message)
        } else {
            ussdController.send(params.removeFirst()) {
                responseInvoke(it)
            }
        }
    }

    override fun over(message: String) {
        respond(this.context, activity.id!!, message)
    }

}