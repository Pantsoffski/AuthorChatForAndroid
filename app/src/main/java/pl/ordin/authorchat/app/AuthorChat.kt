package pl.ordin.authorchat.app

import android.app.Activity
import android.app.Application
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import pl.ordin.authorchat.notifications.NotificationsService
import javax.inject.Inject

class AuthorChat : Application(), HasActivityInjector, HasServiceInjector {

    //region Activity Injector

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityInjector

    //endregion

    //region Service Injector

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector() = serviceInjector

    //endregion

    //region Lifecycle

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency graph
        initializeDependencyInjection()

        // Initialize Notifications Service
        initializeNotificationsService()

        // Initialize tools
        initializeStetho()
    }

    //endregion

    //region Dependency Injection

    private fun initializeDependencyInjection() {
        DaggerAppComponent.builder()
            .applicationContext(this)
            .build()
            .inject(this)
    }

    //endregion

    //region Notifications Service

    private fun initializeNotificationsService() {
        val notificationsJob = JobInfo.Builder(666, ComponentName(this, NotificationsService::class.java))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPeriodic(900000)
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(notificationsJob)
    }

    //endregion

    //region Stetho

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }

    //endregion
}