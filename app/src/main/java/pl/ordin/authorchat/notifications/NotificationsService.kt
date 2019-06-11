package pl.ordin.authorchat.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import pl.ordin.authorchat.R
import pl.ordin.authorchat.main.MainActivity
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiResponse
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class NotificationsService : JobService() {

    @Inject
    lateinit var wordpressApi: WordpressApi

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var observer: Observer<ApiResponse<WordpressApi.Result>>
    private lateinit var observable: LiveData<ApiResponse<WordpressApi.Result>>

    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)
    }


    override fun onStartJob(p0: JobParameters?): Boolean {
        getMessages()

        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        observable.removeObserver(observer)

        return false
    }

    private fun getMessages() {
        observer = Observer {
            it?.let {
                println("Testing")
                addNotification()
            }
        }

        observable = wordpressApi.websiteRest(
            "rooms",
            "",
            sharedPreferencesHelper.usernamePref,
            sharedPreferencesHelper.passwordPref,
            0
        )

        observable.observeForever(observer)
    }

    private fun addNotification() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelID = applicationContext.packageName
        val channelName = applicationContext.getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // set notification channel for oreo+ android version
            val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.notify_panel_notification_icon_bg)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText("You've got new messages!") as NotificationCompat.Builder

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