package pl.ordin.authorchat.main.login

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment
}