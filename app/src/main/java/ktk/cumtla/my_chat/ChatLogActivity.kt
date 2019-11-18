package ktk.cumtla.my_chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import ktk.cumtla.my_chat.adapters.ChatLeftAdapter
import ktk.cumtla.my_chat.adapters.ChatRightAdapter
import ktk.cumtla.my_chat.libby.H
import ktk.cumtla.my_chat.modals.MyMessage
import ktk.cumtla.my_chat.modals.User
import org.jetbrains.anko.toast

class ChatLogActivity : AppCompatActivity() {

    var toUser: User? = null
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        supportActionBar?.title = "Chat Log"

        toUser = intent.getParcelableExtra("toUser")


        btnSend.setOnClickListener {
            sendMessage()
        }

        checkMessageArrive()

        chat_log_recycler.adapter = adapter
    }

    private fun checkMessageArrive() {
        val fromId = H.user?.uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val myMessage = p0.getValue(MyMessage::class.java)
                if (myMessage != null) {
                    if (myMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatRightAdapter(myMessage.text, H.user!!))
                    } else {
                        adapter.add(ChatLeftAdapter(myMessage.text, toUser!!))
                    }
                }
                chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}


            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun sendMessage() {
        val msg = et_chat_message.text.toString()
        val fromId = H.user?.uid
        val toId = toUser?.uid

        val fromRef = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("messages/$toId/$fromId").push()

        val message =
            MyMessage(fromRef.key!!, msg, fromId!!, toId!!, System.currentTimeMillis() / 1000)

        fromRef.setValue(message)


        toRef.setValue(message)

        val latestMessageFromRef =
            FirebaseDatabase.getInstance().getReference("latest-message/$fromId/$toId")
        val latestMessageToRef =
            FirebaseDatabase.getInstance().getReference("latest-message/$toId/$fromId")

        latestMessageFromRef.setValue(message)

        latestMessageToRef.setValue(message)

        et_chat_message.text = null
    }
}
