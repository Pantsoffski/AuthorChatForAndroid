package pl.ordin.utility.sharedpreferences

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SharedPreferencesModule {

    //region SharedPreferencesHelper

    @Provides
    @Singleton
    @JvmStatic
    fun provideSharedPreferencesHelper(context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(context)
    }

    //endregion
}