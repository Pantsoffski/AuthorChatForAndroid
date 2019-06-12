package pl.ordin.authorchat.app

import android.app.Activity
import android.app.Application
import android.app.Service
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
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

    //region Stetho

    private fun initializeStetho() {
        Stetho.initializeWithDefaults(this)
    }

    //endregion
}