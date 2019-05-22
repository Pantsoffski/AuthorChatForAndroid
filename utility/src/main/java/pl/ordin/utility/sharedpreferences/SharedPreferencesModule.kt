package pl.ordin.utility.sharedpreferences

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    //region SharedPreferencesHelper

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(application: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(application)
    }

    //endregion
}