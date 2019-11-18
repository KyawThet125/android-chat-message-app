package ktk.cumtla.my_chat.adapters

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.new_chat_message.view.*
import ktk.cumtla.my_chat.R
import ktk.cumtla.my_chat.modals.User

class UserAdapter(val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.new_chat_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvUsername.text = user.name
        Picasso.get().load(user.image).into(viewHolder.itemView.userProfileImage)
    }

}