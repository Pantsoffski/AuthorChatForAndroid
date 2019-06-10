package pl.ordin.authorchat.app

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: AuthorChat): Context = application
}