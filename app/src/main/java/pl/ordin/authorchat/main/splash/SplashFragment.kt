package pl.ordin.authorchat.main.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ordin.authorchat.R

class SplashFragment : Fragment() {

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        redirectToSignIn()
    }

    //endregion

    //region Redirection

    private fun redirectToSignIn() {
        GlobalScope.launch {
            delay(1000)

            NavHostFragment.findNavController(navHostFragment).navigate(R.id.loginFragment)
        }
    }

    //endregion

}
