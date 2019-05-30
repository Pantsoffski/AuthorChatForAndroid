package pl.ordin.data.network.apiservice

import androidx.lifecycle.LiveData
import pl.ordin.utility.retrofitlivedata.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface WordpressApi {

    //region Wordpress REST

    @POST("wp-json/author-chat/v2/chat")
    fun websiteRest(
        @Query("function") function: String,
        @Query("msg") msg: String,
        @Query("l") login: String,
        @Query("p") password: String,
        @Query("room") room: Int
    ): LiveData<ApiResponse<Result>>

    data class Result(
        val nick: List<String>,
        val msg: List<String>,
        val date: List<String>,
        val room: List<Int>,
        val ver: String,
        val sec: Boolean
    )

    @POST("wp-json/author-chat/v2/chat")
    fun sendMessage(
        @Query("function") function: String,
        @Query("msg") msg: String,
        @Query("l") login: String,
        @Query("p") password: String,
        @Query("room") room: Int
    ): LiveData<ApiResponse<SendResult>>

    data class SendResult(
        val nick: String,
        val msg: String,
        val date: String,
        val room: Int
    )

    //endregion
}