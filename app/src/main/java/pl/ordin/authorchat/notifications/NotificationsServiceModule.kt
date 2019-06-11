package pl.ordin.authorchat.notifications

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NotificationsServiceModule {

    @ContributesAndroidInjector
    abstract fun contributeNotificationsWorker(): NotificationsService
}