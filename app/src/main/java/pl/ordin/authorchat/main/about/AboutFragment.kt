package pl.ordin.authorchat.main.about


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.about_fragment.*
import pl.ordin.authorchat.BuildConfig
import pl.ordin.authorchat.R
import java.util.*

class AboutFragment : Fragment() {

    //region Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        populateUiFields()
    }

    //endregion

    //region Ui

    private fun populateUiFields() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        val versionText = "v" + BuildConfig.VERSION_NAME
        appVersion.text = versionText
        rightsReserved.text = getString(R.string.rights, year)
        contactMe.movementMethod = LinkMovementMethod.getInstance()
    }

    //endregion
}
