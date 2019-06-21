package pl.ordin.authorchat.notifications

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FirebaseMessagingModule {

    @ContributesAndroidInjector
    abstract fun contributeChatFragment(): FirebaseMessagingListenerService

}