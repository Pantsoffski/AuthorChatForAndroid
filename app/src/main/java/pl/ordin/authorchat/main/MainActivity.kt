package pl.ordin.authorchat.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ordin.authorchat.R

class MainActivity : AppCompatActivity() {

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
