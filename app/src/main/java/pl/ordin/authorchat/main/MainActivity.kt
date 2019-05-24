package pl.ordin.authorchat.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ordin.authorchat.R
import pl.ordin.data.network.apiservice.WordpressApi
import pl.ordin.utility.retrofitlivedata.ApiSuccessResponse
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var wordpressApi: WordpressApi

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //DaggerAppComponent.create().injectMain(this)

        AndroidInjection.inject(this)

        Stetho.initializeWithDefaults(this)

        val observable = wordpressApi.getMessages("read", "", "xxx", "xxx", 0)

        observable.observe(this, Observer { result ->
            if (result is ApiSuccessResponse) {
                println("Rezultat: ${result.body}")
            }
        })

        redirect()
    }

    //endregion

    //region UI

    private fun redirect() {
        GlobalScope.launch {
            delay(2000)

            findNavController(navHostFragment).navigate(R.id.loginFragment)
        }
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.splashFragment).navigateUp()

    //endregion

//    fun <VM> xyz(i: Int): VM? {
//        return null
//    }
}

//interface Xyzz<out T> {
//    fun <T> xyz(item: String): T? {
//        val lambda: (String) -> String = { string: String -> string }
//        return null
//    }
//}
