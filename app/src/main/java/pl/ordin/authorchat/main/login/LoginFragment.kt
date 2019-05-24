package pl.ordin.authorchat.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.login_fragment.*
import pl.ordin.authorchat.R

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

//    @Inject
//    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

//    private val viewModel by lazy {
//        ViewModelProviders
//            .of(this, injector.loginViewModelFactory())
//            .get(LoginViewModel::class.java)
//    }

//    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
//
//        val x = sharedPreferencesHelper.websiteUrl
//        println("Rezultat: $x")
    }

    private fun setListeners() {
        signInButton.setOnClickListener {
            //todo save to prefs
            urlPrefixSpinner.selectedItem.toString()
            websiteAddress.text
            username.text
            password.text
            rememberUserCheckBox.text
        }
    }

}
