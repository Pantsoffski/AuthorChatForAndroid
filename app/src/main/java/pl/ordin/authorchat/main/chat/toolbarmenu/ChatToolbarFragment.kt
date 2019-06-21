package pl.ordin.authorchat.main.chat.toolbarmenu


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_chat_toolbar.*
import pl.ordin.authorchat.R
import pl.ordin.utility.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

class ChatToolbarFragment : Fragment() {

    //region ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatToolbarViewModel>
    private lateinit var viewModel: ChatToolbarViewModel

    //endregion

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // must have for dagger injection

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_toolbar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatToolbarViewModel::class.java)

        setUpToolbar()

        startListeners()
    }

    private fun setUpToolbar() {
        toolbar.apply {
            // set title
            title = toolbar.context.getString(R.string.app_name)

            // inflate
            inflateMenu(R.menu.toolbar_chat_menu)

            // set initial value for notifications (check on/off)
            menu.findItem(R.id.notifications).apply {
                isChecked = viewModel.getNotificationsPref()
            }
        }
    }

    private fun startListeners() {
        toolbar.apply {
            // arrow listener
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            // options menu listener
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.notifications -> {
                        // set unchecked if already checked etc.
                        item.isChecked = !item.isChecked

                        viewModel.setNotificationsPref(item.isChecked)
                    }
                    R.id.about -> {
                        // start About fragment
                    }
                }

                true
            }
        }
    }
}
