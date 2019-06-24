package pl.ordin.authorchat.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiErrorResponse
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private var wordpressApi: WordpressApi,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    //region Connection testing

    fun testConnection(): LiveData<String> {
        return Transformations.switchMap(
            wordpressApi.websiteRest(
                sharedPreferencesHelper.websiteCompleteUrl,
                "read",
                "",
                sharedPreferencesHelper.usernamePref,
                sharedPreferencesHelper.passwordPref,
                0
            )
        ) {
            val response = MediatorLiveData<String>()

            when (it) {
                is ApiErrorResponse -> {
                    response.value = it.errorMessage
                    response
                }
                else -> {
                    response.value = "success"
                    response
                }
            }
        }
    }

    //endregion

    //region Shared Preferences handling

    fun saveSignInData(
        websitePrefix: String,
        websiteAddress: String,
        username: String,
        password: String,
        rememberUser: Boolean
    ) {
        with(sharedPreferencesHelper) {
            websiteUrlPrefixPref = websitePrefix
            websiteUrlPref = websiteAddress
            websiteCompleteUrl = "$websitePrefix$websiteAddress/wp-json/author-chat/v2/chat"
            usernamePref = username
            passwordPref = password
            rememberUserPref = rememberUser
        }
    }

    fun getSignInData(): Map<String, Any> {
        with(sharedPreferencesHelper) {
            return mapOf(
                "websitePrefix" to websiteUrlPrefixPref,
                "websiteAddress" to websiteUrlPref,
                "username" to usernamePref,
                "password" to passwordPref,
                "rememberUser" to rememberUserPref
            )
        }
    }

    fun userRemembered(): Boolean {
        return sharedPreferencesHelper.rememberUserPref
    }

    //endregion

}
