package pl.ordin.data.network.apiservice

import androidx.lifecycle.LiveData
import pl.ordin.utility.retrofitlivedata.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface WordpressApi {

    //region Wordpress REST

    @POST
    fun websiteRest(
        @Url url: String,
        @Query("function") function: String,
        @Query("msg") msg: String,
        @Query("l") login: String,
        @Query("p") password: String,
        @Query("room") room: Int
    ): LiveData<ApiResponse<Result>>

    data class Result(
        val id: List<Int>,
        val nick: List<String>,
        val msg: List<String>,
        val date: List<String>,
        val room: List<Int>,
        val ver: String,
        val sec: Boolean
    )

    //endregion
}