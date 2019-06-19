package pl.ordin.authorchat.main.chat.toolbarmenu

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ChatToolbarModule {

    @ContributesAndroidInjector
    abstract fun contributeChatToolbarFragment(): ChatToolbarFragment

}