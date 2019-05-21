package pl.ordin.data.network.http

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class HttpModule {

    //region Retrofit

    @Provides
    @Singleton
    fun provideServerRetrofit(client: OkHttpClient): WordpressApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://xxx.pl/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        println(client)

        return retrofit.create(WordpressApi::class.java)
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