package pl.ordin.authorchat.main.login

import androidx.lifecycle.ViewModel
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val sharedPreferencesHelper: SharedPreferencesHelper) : ViewModel() {

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
}
