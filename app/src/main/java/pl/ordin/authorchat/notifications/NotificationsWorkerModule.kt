package pl.ordin.authorchat.notifications

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NotificationsWorkerModule {

    @ContributesAndroidInjector
    abstract fun contributeNotificationsWorker(): NotificationsWorker
}