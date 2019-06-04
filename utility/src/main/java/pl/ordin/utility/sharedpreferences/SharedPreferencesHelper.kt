package pl.ordin.utility.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Singleton

//region Login Data

const val WEBSITE_PREFIX = "websitePrefix"
const val WEBSITE_URL = "websiteUrl"
const val USERNAME = "username"
const val PASSWORD = "password"
const val REMEMBER_USER = "rememberUser"

//endregion

@Singleton
class SharedPreferencesHelper(c: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(c)

    //region Login Data Shared Prefs getters/setters

    var websiteUrlPrefixPref: String
        get() = prefs.getString(WEBSITE_PREFIX, "")!!
        set(value) {
            prefs.edit()
                .putString(WEBSITE_PREFIX, value)
                .apply()
        }

    var websiteUrlPref: String
        get() = prefs.getString(WEBSITE_URL, "")!!
        set(value) {
            prefs.edit()
                .putString(WEBSITE_URL, value)
                .apply()
        }

    var usernamePref: String
        get() = prefs.getString(USERNAME, "")!!
        set(value) {
            prefs.edit()
                .putString(USERNAME, value)
                .apply()
        }

    var passwordPref: String
        get() = prefs.getString(PASSWORD, "")!!
        set(value) {
            prefs.edit()
                .putString(PASSWORD, value)
                .apply()
        }

    var rememberUserPref: Boolean
        get() = prefs.getBoolean(REMEMBER_USER, false)
        set(value) {
            prefs.edit()
                .putBoolean(REMEMBER_USER, value)
                .apply()
        }

    //endregion
}