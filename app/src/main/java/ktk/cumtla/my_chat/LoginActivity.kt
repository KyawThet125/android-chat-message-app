package ktk.cumtla.my_chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import ktk.cumtla.my_chat.libby.H
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Login"

        toRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val password: String = loginPassword.text.toString()
            val email: String = loginEmail.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                toast("Please fill required fields")
                return@setOnClickListener
            }

            if (password.length < 6) {
                toast("Password required at least 6 characters")
                return@setOnClickListener
            }
            login(email, password)
        }
    }

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(this@LoginActivity, LastChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                H.l("User Login Fail")
            }
    }


}
