package pl.ordin.authorchat.app

import dagger.Component
import pl.ordin.authorchat.main.MainActivity
import pl.ordin.authorchat.main.login.LoginViewModel
import pl.ordin.data.network.http.HttpModule
import pl.ordin.utility.sharedpreferences.SharedPreferencesModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HttpModule::class,
        AppModule::class,
        SharedPreferencesModule::class
    ]
)
interface AppComponent {
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(application: AuthorChat): AppComponent.Builder
//
//        fun build(): AppComponent
//    }
//
//    fun inject(application: AuthorChat)

    fun injectMain(mainActivity: MainActivity)

    fun injectLoginViewModel(loginViewModel: LoginViewModel)

}