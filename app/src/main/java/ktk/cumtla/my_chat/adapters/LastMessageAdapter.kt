package ktk.cumtla.my_chat.adapters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.last_message_row.view.*
import ktk.cumtla.my_chat.R
import ktk.cumtla.my_chat.modals.MyMessage
import ktk.cumtla.my_chat.modals.User

class LastMessageAdapter(val myMessage: MyMessage) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.last_message_row
    }

    var friendUser : User? = null


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.tvUserMessage.text = myMessage.text

        val friendId: String
        if (myMessage.fromId == FirebaseAuth.getInstance().uid) {
            friendId = myMessage.toId
        } else {
            friendId = myMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("users/$friendId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                friendUser = p0.getValue(User::class.java)
                viewHolder.itemView.tvUsername.text = friendUser?.name
                Picasso.get().load(friendUser?.image).into(viewHolder.itemView.last_chat_image)
            }

        })
    }
}