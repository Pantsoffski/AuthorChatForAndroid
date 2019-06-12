package pl.ordin.authorchat.main.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiResponse
import pl.ordin.utility.retrofitlivedata.ApiSuccessResponse
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private var wordpressApi: WordpressApi,
    private var sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private var lastMessages: MutableMap<Int, WebsiteAnswer>? = null // necessarily to compare to push only new messages

    fun getMessages(): LiveData<Map<Int, WebsiteAnswer>?> {
        return switchMap(
            wordpressApi.websiteRest(
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) {
            val data = MediatorLiveData<Map<Int, WebsiteAnswer>?>()

            if (it is ApiSuccessResponse) {

                //val messagesToPush = WebsiteAnswer(it.body.nick, it.body.date, it.body.msg, it.body.room)
                var messagesToPush = mutableMapOf<Int, WebsiteAnswer>()

                for (i in it.body.id.indices) {
                    messagesToPush[it.body.id[i]] =
                        WebsiteAnswer(
                            it.body.nick[i],
                            it.body.date[i],
                            it.body.msg[i],
                            it.body.room[i]
                        )
                }

                if (lastMessages == null) { // populate lastMessages if null
                    lastMessages = messagesToPush
                } else { // filter
                    messagesToPush = messagesToPush.filterKeys { key ->
                        !lastMessages!!.containsKey(key)
                    } as MutableMap<Int, WebsiteAnswer>

                    lastMessages!!.putAll(messagesToPush)
                }

                data.postValue(messagesToPush)
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

    fun clearLastMessages() {
        lastMessages?.let {
            it.keys.removeAll(it.keys)
        }
    }

    //region Notifications Service

    fun subscribeToNotifications() {
        val topic = sharedPreferencesHelper.websiteUrlPref

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Log.i("Topic subscription was successful to topic", topic)
                else
                    Log.e("Topic subscription was not successful to topic", topic)
            }
    }

    //endregion

    data class WebsiteAnswer(
        val nick: String,
        val date: String,
        val msg: String,
        val room: Int
    )
}
