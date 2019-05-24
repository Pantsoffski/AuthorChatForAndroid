package pl.ordin.authorchat.main.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.login_fragment.*
import pl.ordin.authorchat.R
import pl.ordin.utility.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<LoginViewModel>

//    @Inject
//    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

//    private val viewModel by lazy {
//        ViewModelProviders
//            .of(this, viewModelFactory)
//            .get(LoginViewModel::class.java)
//    }

    private lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

//        val x = sharedPreferencesHelper.websiteUrl
//        println("Rezultat: $x")

        viewModel.test()
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
