package pl.ordin.authorchat.main.chat

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ChatModule {

    @ContributesAndroidInjector
    abstract fun contributeChatFragment(): ChatFragment
}