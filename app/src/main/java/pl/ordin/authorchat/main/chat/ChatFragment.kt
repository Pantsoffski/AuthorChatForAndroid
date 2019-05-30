package pl.ordin.authorchat.main.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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

        startListeners()
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

        viewModel.getMessages(0).observe(this, Observer { result ->
            result?.let {
                for (i in it.first.indices) {
                    groupAdapter.add(MessageItem(it.first[i], it.second[i], it.third[i]))
                    //todo first data must use addAll, next at every update only add
                }
            }
        })

        //endregion

        //region Get rooms for user

        viewModel.getRooms().observe(this, Observer { result ->
            result?.let {
                println("Rooms: $it") //todo populate buttons with rooms
            }
        })

        //endregion
    }

    private fun startListeners() {
        sendButton.setOnClickListener {
            val textToSend = messageEditText.text.toString()

            viewModel.sendMessage(0, textToSend).observe(this, Observer {
                println(it)
            })

            messageEditText.text.clear()
        }
    }

    //endregion


//    private fun populateMessages(): MutableList<MessageItem> {
//        return MutableList(15) {
//            MessageItem()
//        }
//    }
}
