package pl.ordin.authorchat.main.chat.item

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_message.*
import pl.ordin.authorchat.R

class MessageItem(
    private val nick: String,
    private val date: String,
    private val message: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            nickView.text = nick
            dateView.text = date
            chatView.text = message
        }
    }

    override fun getLayout(): Int = R.layout.item_message
}