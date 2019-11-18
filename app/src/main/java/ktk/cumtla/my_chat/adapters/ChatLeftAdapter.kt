package ktk.cumtla.my_chat.adapters

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_left_row.view.*
import ktk.cumtla.my_chat.R
import ktk.cumtla.my_chat.modals.User

class ChatLeftAdapter(val message: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_left_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_left_msg.text = message
        Picasso.get().load(user.image).into(viewHolder.itemView.chat_left_image)
    }
}