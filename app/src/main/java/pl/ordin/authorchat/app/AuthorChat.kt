package pl.ordin.authorchat.app

import android.app.Application

class AuthorChat : Application() {
    //val context: Context =  this.applicationContext

    companion object {
        fun newInstance() = AuthorChat()
    }

//    override fun onCreate() {
//        super.onCreate()
//
//
//    }
}