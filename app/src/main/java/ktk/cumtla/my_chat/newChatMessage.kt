package ktk.cumtla.my_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_chat_message.*
import ktk.cumtla.my_chat.libby.H
import ktk.cumtla.my_chat.modals.User
import ktk.cumtla.my_chat.adapters.UserAdapter

class newChatMessage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_message)
        supportActionBar?.title = "New MyMessage"

        fetchAllUser()

    }

    private fun fetchAllUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                H.l("Data BAse error ")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.name != H.user?.name) {
                        adapter.add(UserAdapter(user))
                    }

                }
                adapter.setOnItemClickListener { item, view ->
                    val toUser = item as UserAdapter
                    val intent = Intent(this@newChatMessage, ChatLogActivity::class.java)
                    intent.putExtra("toUser", toUser.user)
                    startActivity(intent)
                }


                new_message_recycler.adapter = adapter
            }

        })
    }
}
