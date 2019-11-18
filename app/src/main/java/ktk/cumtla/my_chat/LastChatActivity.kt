package ktk.cumtla.my_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_last_chat.*
import ktk.cumtla.my_chat.adapters.LastMessageAdapter
import ktk.cumtla.my_chat.libby.H.Companion.user
import ktk.cumtla.my_chat.modals.MyMessage
import ktk.cumtla.my_chat.modals.User

class LastChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessageMap = HashMap<String, MyMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_chat)
        supportActionBar?.title = "Last Chat Message"

        last_chat_message_recycler.adapter = adapter

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LastMessageAdapter
            intent.putExtra("toUser", row.friendUser)
            startActivity(intent)
        }

        checkUserAuth()
        fetchUser()
        listenForLatestMessage()
    }

    private fun refreshRecyclerView() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LastMessageAdapter(it))
        }
    }

    private fun listenForLatestMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("latest-message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val myMessage = p0.getValue(MyMessage::class.java)
                if (myMessage != null) {
                    latestMessageMap[p0.key!!] = myMessage
                }
                refreshRecyclerView()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val myMessage = p0.getValue(MyMessage::class.java)
                if (myMessage != null) {
                    latestMessageMap[p0.key!!] = myMessage
                }
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)
            }

        })

    }

    private fun checkUserAuth() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this@LastChatActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start_message -> {
                val intent = Intent(this@LastChatActivity, newChatMessage::class.java)
                startActivity(intent)
            }
            R.id.user_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@LastChatActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
