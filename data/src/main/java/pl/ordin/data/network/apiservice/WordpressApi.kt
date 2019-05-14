package pl.ordin.data.network.apiservice

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query

interface WordpressApi {

    //region Wordpress

    @GET("api.php")
    fun <T> hitCountCheck(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("list") list: String,
        @Query("srsearch") srsearch: String
    ): LiveData<T>

    //endregion
}