package pl.ordin.authorchat.app

import android.app.Activity
import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.ordin.authorchat.notifications.NotificationsWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthorChat : Application(), HasActivityInjector {

    //region Activity Injector

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityInjector

    //endregion

    //region Lifecycle

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency graph
        initializeDependencyInjection()

        // Initialize Notifications Worker
        initializeNotificationsWorker()

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

    //region Notifications Worker

    private fun initializeNotificationsWorker() {
        val notificationsWork = PeriodicWorkRequestBuilder<NotificationsWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance().enqueue(notificationsWork)
    }

    //endregion

    //region Stetho

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }

    //endregion
}