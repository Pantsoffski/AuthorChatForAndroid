package pl.ordin.data.network.http

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.LiveDataCallAdapterFactory
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class HttpModule {

    //region Retrofit

    @Provides
    @Singleton
    fun provideServerRetrofit(client: OkHttpClient, url: String): WordpressApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        println(client)

        return retrofit.create(WordpressApi::class.java)
    }

    @Provides
    @Singleton
    fun getWebsiteUrl(context: Context): String {
        val sharedPreferencesHelper = SharedPreferencesHelper(context)

        return with(sharedPreferencesHelper) {
            websiteUrlPrefixPref + websiteUrlPref
        }
    }

    //endregion

    //region Retrofit - Http

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(logging)
            .build()
    }

    //endregion
}