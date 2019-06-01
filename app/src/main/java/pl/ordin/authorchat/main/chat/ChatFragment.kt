package pl.ordin.authorchat.main.chat

import android.content.Context
import android.content.res.Resources.getSystem
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.chat_fragment.*
import pl.ordin.authorchat.R
import pl.ordin.authorchat.main.chat.item.MessageItem
import pl.ordin.utility.viewmodelfactory.ViewModelFactory
import javax.inject.Inject

class ChatFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatViewModel>

    private lateinit var viewModel: ChatViewModel

    private val groupAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    private lateinit var messagesObserver: Observer<ChatViewModel.WebsiteAnswer?>

    private var activeRoom = 0

    //region Lifecycle

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this) // must have for dagger injection
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel::class.java)

        setUpGroupie()

        startObservers()

        setMainRoomSendButtonsListeners()
    }

    //endregion

    //region Start due lifecycle

    private fun setUpGroupie() {
        //groupAdapter.add(MessageItem("1", "2", "3"))

        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatFragment.context)
            adapter = groupAdapter
        }
    }

    private fun startObservers() {

        //region Get messages

        messagesObserver = Observer { result ->
            viewModel.getMessages().removeObserver(messagesObserver)

            result?.let {
                groupAdapter.clear()

                for (i in it.room.indices) {
                    if (activeRoom == it.room[i]) //filter to one room
                        groupAdapter.add(MessageItem(it.nick[i], it.date[i], it.msg[i]))
                    //todo first data must use addAll, next at every update only add
                }
            }
        }

        viewModel.getMessages().observe(this, messagesObserver)

        //endregion

        //region Get rooms for user

        viewModel.getRooms().observe(this, Observer { result ->
            result?.let {

                for (i in it.indices) {
                    val btn = roomButtonStyler()
                    btn.text = getString(R.string.chat_button_room, i + 1)
                    btn.tag = it[i] //keep room number in button tag
                    setRoomButtonListener(btn)
                    flexboxLayoutRoomsButtonsContainer.addView(btn)
                }

            }
        })

        //endregion
    }

    private fun setMainRoomSendButtonsListeners() {
        mainRoomButton.setOnClickListener {
            activeRoom = 0

            viewModel.getMessages().observe(this, messagesObserver)
        }

        sendButton.setOnClickListener {
            val textToSend = messageEditText.text.toString()

            viewModel.sendMessage(activeRoom, textToSend).observe(this, Observer {
                println(it)
            })

            messageEditText.text.clear()
        }
    }

    private fun setRoomButtonListener(btn: MaterialButton) {
        btn.setOnClickListener {
            activeRoom = btn.tag as Int

            viewModel.getMessages().observe(this, messagesObserver)
        }
    }

    //endregion

    private fun roomButtonStyler(): MaterialButton {
        val button = MaterialButton(ContextThemeWrapper(this.context, R.style.MaterialButtonsStyle), null, 0)
        val layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(2.toPx())

        // called separately because this from MaterialButtonsStyle doesn't work in ContextThemeWrapper
        button.apply {
            cornerRadius = 20
            setLayoutParams(layoutParams)
        }

        return button
    }

    // dp to px converter
    private fun Int.toPx(): Int = (this * getSystem().displayMetrics.density).toInt()
}
