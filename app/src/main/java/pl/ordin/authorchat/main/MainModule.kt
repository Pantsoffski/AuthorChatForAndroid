package pl.ordin.authorchat.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

}