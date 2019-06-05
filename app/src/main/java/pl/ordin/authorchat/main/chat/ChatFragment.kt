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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ordin.authorchat.R
import pl.ordin.authorchat.main.chat.item.MessageItem
import pl.ordin.utility.viewmodelfactory.ViewModelFactory
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class ChatFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatViewModel>

    private lateinit var viewModel: ChatViewModel

    private val groupAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    private lateinit var messagesObserver: Observer<Map<Int, ChatViewModel.WebsiteAnswer>?>

    private var activeRoom = 0

    private lateinit var messagesRefresher: Timer

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

        refresher()
    }

    override fun onDestroy() {
        super.onDestroy()

        messagesRefresher.cancel()
    }

    //endregion

    //region Start due lifecycle

    private fun setUpGroupie() {
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
                //groupAdapter.clear()

                for (item in it) {
                    if (activeRoom == item.value.room) //filter to one room
                        groupAdapter.add(MessageItem(item.value.nick, item.value.date, item.value.msg))

                    progressBar.visibility = View.GONE //remove progress bar

                    moveToLastPosition()
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

            doAfterRoomButtonPressed()
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

            doAfterRoomButtonPressed()
        }
    }

    private fun refresher() {
        messagesRefresher = fixedRateTimer(
            name = "messages-refresher",
            initialDelay = 3000,
            period = 5000
        ) {
            // Dispatchers.Main to observe at main thread
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.getMessages().observe(this@ChatFragment, messagesObserver)
            }
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

    private fun doAfterRoomButtonPressed() {
        groupAdapter.clear()

        progressBar.visibility = View.VISIBLE

        viewModel.getMessages().observe(this, messagesObserver)

        viewModel.clearLastMessages()
    }

    private fun moveToLastPosition() =
        chatRecyclerView.smoothScrollToPosition(if (groupAdapter.itemCount == 0) groupAdapter.itemCount else groupAdapter.itemCount - 1)

    // dp to px converter
    private fun Int.toPx(): Int = (this * getSystem().displayMetrics.density).toInt()
}
