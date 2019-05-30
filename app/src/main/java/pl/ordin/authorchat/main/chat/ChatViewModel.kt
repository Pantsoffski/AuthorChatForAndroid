package pl.ordin.authorchat.main.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun getMessages(room: Int): LiveData<Triple<List<String>, List<String>, List<String>>?> {
        return Transformations.switchMap(
            wordpressApi.websiteRest(
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                room
            )
        ) {
            val data = MutableLiveData<Triple<List<String>, List<String>, List<String>>?>()

            if (it is ApiSuccessResponse) {
                data.postValue(Triple(it.body.nick, it.body.date, it.body.msg))
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
}
