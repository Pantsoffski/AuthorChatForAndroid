package pl.ordin.data.network.apiservice

import androidx.lifecycle.LiveData
import pl.ordin.utility.retrofitlivedata.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WordpressApi {

    //region Wordpress

    @GET("api.php")
    fun hitCountCheck(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("list") list: String,
        @Query("srsearch") srsearch: String
    ): LiveData<ApiResponse<Model.Result>>

    object Model {
        data class Result(val query: Query)
        data class Query(val searchinfo: SearchInfo)
        data class SearchInfo(val totalhits: Int)
    }

    //endregion
}