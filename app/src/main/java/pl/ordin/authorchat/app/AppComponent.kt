package pl.ordin.authorchat.app

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import pl.ordin.authorchat.main.MainModule
import pl.ordin.authorchat.main.chat.ChatModule
import pl.ordin.authorchat.main.login.LoginModule
import pl.ordin.data.network.http.HttpModule
import pl.ordin.utility.sharedpreferences.SharedPreferencesModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        MainModule::class,
        LoginModule::class,
        ChatModule::class,
        HttpModule::class,
        SharedPreferencesModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): AppComponent
    }

    fun inject(authorChat: AuthorChat)
}