package ktk.cumtla.my_chat.adapters

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.message_right_row.view.*
import ktk.cumtla.my_chat.R
import ktk.cumtla.my_chat.modals.User


class ChatRightAdapter(val message: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_right_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_right_msg.text = message
        Picasso.get().load(user.image).into(viewHolder.itemView.chat_right_image)
    }
}