package pl.ordin.authorchat.app

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    //region Application Context

    @Provides
    @Singleton
    fun provideContext(): Context {
        return AuthorChat.newInstance().applicationContext
    }

    //endregion

}