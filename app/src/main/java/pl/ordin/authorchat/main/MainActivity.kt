package pl.ordin.authorchat.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pl.ordin.authorchat.R
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    //region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //endregion

    //region UI

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.splashFragment).navigateUp()

    //endregion

    override fun supportFragmentInjector() = fragmentInjector

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
