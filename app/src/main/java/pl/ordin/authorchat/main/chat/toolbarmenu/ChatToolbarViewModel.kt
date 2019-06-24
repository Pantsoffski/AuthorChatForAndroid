package pl.ordin.authorchat.main.chat.toolbarmenu

import androidx.lifecycle.ViewModel
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class ChatToolbarViewModel @Inject constructor(
    private var sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    //region Notifications Preferences

    fun setNotificationsPref(setting: Boolean) {
        sharedPreferencesHelper.notifications = setting
    }

    fun getNotificationsPref(): Boolean = sharedPreferencesHelper.notifications

    //endregion

}