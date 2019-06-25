package pl.ordin.authorchat.main.chat.item

import android.graphics.Color
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_message.*
import pl.ordin.authorchat.R
import kotlin.random.Random


class MessageItem(
    private val nick: String,
    private val date: String,
    private val message: String
) : Item() {

    //region Single Message

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            nickView.text = nick
            nickView.setTextColor(hexColorGenerator(nick)) // set individual color for each nick
            dateView.text = date
            chatView.text = message
        }
    }

    override fun getLayout(): Int = R.layout.item_message

    //generate rgb color (string as a seed)
    private fun hexColorGenerator(seed: String): Int {
        val hash = seed.hashCode().toLong()
        val rnd = Random(hash)

        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    //endregion
}