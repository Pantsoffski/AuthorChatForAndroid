package pl.ordin.authorchat.main.login

import androidx.lifecycle.ViewModel
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    fun saveSignInData() {
        val x = sharedPreferencesHelper.websiteUrl
        println("Rezultat: $x")
    }
}
