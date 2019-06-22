package pl.ordin.authorchat.main.chat

import android.content.Context
import android.content.res.Resources.getSystem
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
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

    //region ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ChatViewModel>
    private lateinit var viewModel: ChatViewModel

    //endregion

    //region RecyclerView

    private val groupAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    //endregion

    //region Observers

    private lateinit var messagesObserver: Observer<Map<Int, ChatViewModel.WebsiteAnswer>?>
    private lateinit var roomsObserver: Observer<List<Int>?>

    //endregion

    //region Rooms status

    private var activeRoom = 0
    private var activeButtons = mutableListOf<MaterialButton>()

    //endregion

    //region Refreshers

    private lateinit var messagesRefresher: Timer
    private lateinit var roomsRefresher: Timer

    //endregion

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

        startRefreshers()

        viewModel.subscribeToNotifications()
    }

    override fun onDestroy() {
        super.onDestroy()

        messagesRefresher.cancel()
        roomsRefresher.cancel()
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

        //Get messages

        messagesObserver = Observer { result ->

            viewModel.getMessages().removeObserver(messagesObserver)

            result?.let {

                //remove progress bar
                progressBar.visibility = View.GONE

                for (item in it) {
                    if (activeRoom == item.value.room) //filter to one room
                        groupAdapter.add(MessageItem(item.value.nick, item.value.date, item.value.msg))

                    //move to last position
                    chatRecyclerView.smoothScrollToPosition(
                        if (groupAdapter.itemCount == 0)
                            groupAdapter.itemCount
                        else
                            groupAdapter.itemCount - 1
                    )
                }
            }
        }

        viewModel.getMessages().observe(this, messagesObserver)

        //Get rooms for user

        roomsObserver = Observer { result ->
            result?.let {
                //clean activeButtons
                activeButtons.removeAll(activeButtons)

                //add main button to list
                activeButtons.add(mainRoomButton)

                //remove old rooms before add new ones
                flexboxLayoutRoomsButtonsContainer.removeViewsInLayout(
                    1,
                    flexboxLayoutRoomsButtonsContainer.flexItemCount - 1
                )

                for (i in it.indices) {
                    val btn = roomButtonStyler()
                    btn.text = getString(R.string.chat_button_room, i + 1)
                    btn.tag = it[i] //keep room number in button tag
                    setRoomButtonListener(btn)
                    flexboxLayoutRoomsButtonsContainer.addView(btn)

                    //add button to list
                    activeButtons.add(btn)

                    buttonHighlighter()
                }
            }
        }

        viewModel.getRooms().observe(this, roomsObserver)
    }

    private fun setMainRoomSendButtonsListeners() {
        mainRoomButton.tag = 0 // set main button tag to 0 Int (in xml you can set only strong)

        mainRoomButton.setOnClickListener {
            activeRoom = 0

            buttonHighlighter()

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

            buttonHighlighter()

            doAfterRoomButtonPressed()
        }
    }

    private fun startRefreshers() {
        messagesRefresher = fixedRateTimer(
            name = "messages-startRefreshers",
            initialDelay = 3000,
            period = 5000
        ) {
            // Dispatchers.Main to observe at main thread
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.getMessages().observe(this@ChatFragment, messagesObserver)
            }
        }

        roomsRefresher = fixedRateTimer(
            name = "rooms-startRefreshers",
            initialDelay = 5000,
            period = 9000
        ) {
            // Dispatchers.Main to observe at main thread
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.getRooms().observe(this@ChatFragment, roomsObserver)
            }
        }
    }

    //endregion

    //region Buttons handling

    private fun buttonHighlighter() {
        //set background color to make button highlighted
        activeButtons.map { btn ->
            if (btn.tag != activeRoom)
                btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            else
                btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        }
    }

    private fun roomButtonStyler(): MaterialButton {
        val button = MaterialButton(ContextThemeWrapper(this.context, R.style.MaterialButtonsStyle), null, 0)
        val layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.topMargin = (-5).toPx()
        layoutParams.bottomMargin = (-6).toPx()
        layoutParams.marginStart = 1.toPx()
        layoutParams.marginEnd = 1.toPx()

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

    //endregion

    //region DP/PX converter

    private fun Int.toPx(): Int = (this * getSystem().displayMetrics.density).toInt()

    //endregion
}
