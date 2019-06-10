package pl.ordin.authorchat.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.sharedpreferences.SharedPreferencesHelper
import javax.inject.Inject

class NotificationsWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @Inject
    lateinit var wordpressApi: WordpressApi

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun doWork(): Result {
        //AndroidInjection.inject(this as Service)

        getMessages()

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()


    }

    private fun getMessages() {
        val y = sharedPreferencesHelper.usernamePref

        val x = wordpressApi.websiteRest(
            "rooms",
            "",
            sharedPreferencesHelper.usernamePref,
            sharedPreferencesHelper.passwordPref,
            0
        )

        println(y)
    }
}