package pl.ordin.authorchat.main.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*
import pl.ordin.authorchat.R
import pl.ordin.utility.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<LoginViewModel>

    private lateinit var viewModel: LoginViewModel

    //region Lifecycle

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // must have for dagger injection
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        redirectToChat()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getLastSignInDataIfExists()

        setListeners()
    }

    //endregion

    //region Populate fields with data from preferences

    private fun getLastSignInDataIfExists() {
        val signInData = viewModel.getSignInData()

        if (signInData["websiteAddress"].toString().isNotEmpty()) {
            urlPrefixSpinner.setSelection(
                if (signInData["websitePrefix"] == "https://")
                    0
                else
                    1
            )

            websiteAddress.setText(signInData["websiteAddress"] as String)
            username.setText(signInData["username"] as String)
            password.setText(signInData["password"] as String)
            rememberUserCheckBox.isChecked = signInData["rememberUser"] as Boolean
        }
    }

    //endregion

    //region Set Listeners

    private fun setListeners() {
        signInButton.setOnClickListener {
            // save login data to shared prefs
            viewModel.saveSignInData(
                websitePrefix = urlPrefixSpinner.selectedItem.toString(),
                websiteAddress = websiteAddress.text.toString(),
                username = username.text.toString(),
                password = password.text.toString(),
                rememberUser = rememberUserCheckBox.isChecked
            )

            // redirect to chat
            NavHostFragment.findNavController(navHostFragment).navigate(R.id.chatFragment)
        }
    }

    //endregion

    //region Redirect

    // redirect to chat instantly if user checked "remember user" option
    private fun redirectToChat() {
        if (viewModel.userRemembered())
            NavHostFragment.findNavController(navHostFragment).navigate(R.id.chatFragment) // redirect to chat
    }

    //endregion
}
