package pl.ordin.utility.sharedpreferences

import android.app.Application
import android.preference.PreferenceManager

//region Login Data

const val WEBSITE_URL = "websiteUrl"
const val USERNAME = "username"
const val PASSWORD = "password"
const val REMEMBER_USER = "rememberUser"

//endregion

class SharedPreferencesHelper(a: Application) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(a)

    //region Login Data Shared Prefs getters/setters

    var websiteUrl: String
        get() = prefs.getString(WEBSITE_URL, "")!!
        set(value) {
            prefs.edit()
                .putString(WEBSITE_URL, value)
                .apply()
        }

    var username: String
        get() = prefs.getString(USERNAME, "")!!
        set(value) {
            prefs.edit()
                .putString(USERNAME, value)
                .apply()
        }

    var password: String
        get() = prefs.getString(PASSWORD, "")!!
        set(value) {
            prefs.edit()
                .putString(PASSWORD, value)
                .apply()
        }

    var rememberUser: Boolean
        get() = prefs.getBoolean(REMEMBER_USER, true)
        set(value) {
            prefs.edit()
                .putBoolean(REMEMBER_USER, value)
                .apply()
        }

    //endregion
}