package pl.ordin.authorchat.main.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiSuccessResponse
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private var wordpressApi: WordpressApi,
    private var sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    fun getMessages(room: Int): LiveData<Triple<List<String>, List<String>, List<String>>?> {

//        fun returnTriple(liveData: LiveData<ApiResponse<WordpressApi.Result>>): LiveData<Triple<List<String>, List<String>, List<String>>> {
//
//        }

//        Transformations.switchMap(wordpressApi.getMessages("read", "", "xx", "xxx", 0)) {
//            if (it is ApiSuccessResponse)
//                Triple(it.body.nick, it.body.date, it.body.msg)
//            else
//                Triple(null, null, null)
//        }

        return Transformations.map(
            wordpressApi.getMessages(
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                room
            )
        ) {
            if (it is ApiSuccessResponse)
                Triple(it.body.nick, it.body.date, it.body.msg)
            else
                null
        }
    }
}
