package pl.ordin.data.network.apiservice

import androidx.lifecycle.LiveData
import pl.ordin.utility.retrofitlivedata.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface WordpressApi {

    //region Wordpress

    @POST("wp-json/author-chat/v2/chat")
    fun getChat(
        @Query("msg") msg: String,
        @Query("l") login: String,
        @Query("p") password: String
    ): LiveData<ApiResponse<Result>>

    data class Result(
        val nick: List<String>,
        val msg: List<String>,
        val date: List<String>
    )

    //endregion
}