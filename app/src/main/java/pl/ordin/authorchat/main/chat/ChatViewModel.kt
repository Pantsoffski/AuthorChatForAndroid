package pl.ordin.authorchat.main.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiResponse
import pl.ordin.utility.retrofitlivedata.ApiSuccessResponse
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private var wordpressApi: WordpressApi,
    private var sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

//    val messages = MediatorLiveData<WebsiteAnswer?>()
//
//    private val websiteMessagesServiceAnswer = Transformations.map(
//        wordpressApi.websiteRest(
//            "read",
//            "",
//            sharedPreferencesHelper.usernamePref,
//            sharedPreferencesHelper.passwordPref,
//            0
//        )
//    ) {
//        if (it is ApiSuccessResponse) {
//            WebsiteAnswer(it.body.nick, it.body.date, it.body.msg, it.body.room)
//        } else
//            null
//    }

    fun getMessages(): LiveData<WebsiteAnswer?> {
        return Transformations.switchMap(
            wordpressApi.websiteRest(
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) {
            val data = MediatorLiveData<WebsiteAnswer?>()

            if (it is ApiSuccessResponse) {
                data.postValue(WebsiteAnswer(it.body.nick, it.body.date, it.body.msg, it.body.room))
                data
            } else
                null
        }
    }

    fun sendMessage(room: Int, message: String): LiveData<ApiResponse<WordpressApi.Result>> {
        return wordpressApi.websiteRest(
            "send",
            message,
            sharedPreferencesHelper.usernamePref,
            sharedPreferencesHelper.passwordPref,
            room
        )
    }

    fun getRooms(): LiveData<List<Int>?> {
        return Transformations.map(
            wordpressApi.websiteRest(
                "rooms",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) {
            if (it is ApiSuccessResponse)
                it.body.room
            else
                null
        }
    }

//    fun refreshMessages() = websiteMessagesServiceAnswer?.let {
//        messages.value = it.value
//    }

    data class WebsiteAnswer(
        val nick: List<String>,
        val date: List<String>,
        val msg: List<String>,
        val room: List<Int>
    )
}
