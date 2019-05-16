package pl.ordin.data.network.http

import dagger.Module
import dagger.Provides
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
    fun provideServerRetrofit(): WordpressApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/w/")
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WordpressApi::class.java)
    }

    //endregion

    //region Retrofit - Factories


    //endregion
}