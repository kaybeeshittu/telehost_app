package ng.myflex.telehost.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ng.myflex.telehost.R
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.ui.MainActivity
import java.io.Closeable
import javax.inject.Inject


class NotificationService @Inject constructor(
    private val context: Context
) : Closeable {
    companion object {
        private val tag = NotificationService::class.java.name

        const val notificationId = 0x000001
        const val notificationName = "Telehost Channel"
    }

    private lateinit var account: Account

    private var finished: Boolean = true

    private var isConnected: Boolean = false

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun initialize(account: Account) {
        this.finished = false
        this.account = account

        createNotification()
    }

    private fun createNotification() {
        val code = 0
        val flag = 0
        val intent = Intent(context, MainActivity::class.java)
        val notification = NotificationCompat.Builder(context, tag)
            .apply {
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setContentTitle(account.email)
                setContentText(
                    if (!isConnected) {
                        "Disconnected from telehost service"
                    } else "Connected to telehost service"
                )
                setOngoing(true)
                setContentIntent(PendingIntent.getActivities(context, code, arrayOf(intent), flag))
            }.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        notificationManager.notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            tag, notificationName, NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun setIsConnected(isConnected: Boolean) {
        this.isConnected = isConnected

        if (!finished) {
            createNotification()
        }
    }

    override fun close() {
        finished = true
        notificationManager.cancel(notificationId)
    }
}