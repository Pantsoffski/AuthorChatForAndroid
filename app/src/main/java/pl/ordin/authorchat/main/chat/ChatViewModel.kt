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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private var wordpressApi: WordpressApi,
    private var sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    //region Last Messages

    private var lastMessages: MutableMap<Int, WebsiteAnswer>? = null // necessarily to compare to push only new messages

    //endregion

    //region Messages Handling

    fun getMessages(): LiveData<Map<Int, WebsiteAnswer>?> {
        return switchMap(
            wordpressApi.websiteRest(
                sharedPreferencesHelper.websiteCompleteUrl,
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) {
            val data = MediatorLiveData<Map<Int, WebsiteAnswer>?>()

            var messagesToPush = mutableMapOf<Int, WebsiteAnswer>()

            when (it) {
                is ApiSuccessResponse -> {
                    for (i in it.body.id.indices) {
                        messagesToPush[it.body.id[i]] =
                            WebsiteAnswer(
                                it.body.nick[i],
                                datePreparation(it.body.date[i]), // convert UTC date to local user date
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
                }
                else -> null
            }
        }
    }

    fun sendMessage(room: Int, message: String): LiveData<ApiResponse<WordpressApi.Result>> {
        return wordpressApi.websiteRest(
            sharedPreferencesHelper.websiteCompleteUrl,
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
                sharedPreferencesHelper.websiteCompleteUrl,
                "rooms",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) { response ->
            if (response is ApiSuccessResponse)
                response.body.room
            else
                null
        }
    }

    fun clearLastMessages() {
        lastMessages?.let {
            it.keys.removeAll(it.keys)
        }
    }

    private fun datePreparation(inDate: String): String {
        val parser = SimpleDateFormat("Y-m-d,H:m:s", Locale.ENGLISH)
        parser.timeZone = TimeZone.getTimeZone("UTC")

        var date = parser.parse(inDate)

        // make sure to add 1 hour if there is Daylight Saving Time
        if (TimeZone.getDefault().useDaylightTime()) {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.HOUR_OF_DAY, 1)
            date = cal.time
        }

        parser.timeZone = TimeZone.getDefault()

        return parser.format(date)
    }

    //endregion

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

    //region Website Answer Data Class

    data class WebsiteAnswer(
        val nick: String,
        val date: String,
        val msg: String,
        val room: Int
    )

    //endregion
}
