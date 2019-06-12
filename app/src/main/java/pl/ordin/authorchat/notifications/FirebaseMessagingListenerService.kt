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
import pl.ordin.authorchat.R
import pl.ordin.authorchat.main.MainActivity

class FirebaseMessagingListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let {
            val title = it.notification?.title ?: "Backup title"
            val message = it.notification?.body ?: "Backup message"
            sendNotification(title, message)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = applicationContext.packageName
        val channelName = applicationContext.getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // set notification channel for oreo+ android version
            val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.notify_panel_notification_icon_bg)
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
}