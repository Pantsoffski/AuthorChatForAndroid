package pl.ordin.authorchat.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import pl.ordin.authorchat.R
import pl.ordin.authorchat.main.MainActivity
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class FirebaseMessagingListenerService : FirebaseMessagingService() {

    //region Shared Preferences Helper

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    //endregion

    //region Lifecycle

    override fun onCreate() {
        super.onCreate()

        // Inject dependencies
        AndroidInjection.inject(this)
    }

    //endregion

    //region New message listener

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (sharedPreferencesHelper.notifications)
            remoteMessage?.let {
                val title = it.notification?.title ?: "No title"
                val message = it.notification?.body ?: "No message"
                sendNotification(title, message)
            }
    }

    //endregion

    //region Notification

    private fun sendNotification(title: String, message: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = applicationContext.packageName
        val channelName = applicationContext.getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // set notification channel for oreo+ android version
            val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.ic_twotone_message)
            .setContentTitle(title)
            .setContentText(message) as NotificationCompat.Builder

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            applicationContext, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)
        builder.setAutoCancel(true) //to remove notification after click

        // Add as notification
        manager.notify(0, builder.build())
    }

    //endregion

}