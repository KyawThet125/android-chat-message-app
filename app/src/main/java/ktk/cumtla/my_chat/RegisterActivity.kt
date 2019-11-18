package ktk.cumtla.my_chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import ktk.cumtla.my_chat.libby.H
import ktk.cumtla.my_chat.modals.User
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.io.InputStream
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "Register"

        toLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        profileImage.setOnClickListener {
            requestPermission()
        }
        tvImageUpload.setOnClickListener {
            toast("You clicked profile image to Upload")
        }


        registerBtn.setOnClickListener {
            val username: String = register_username.text.toString()
            val email: String = registerEmail.text.toString()
            val password: String = register_password.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                toast("Please fill required fields")
                return@setOnClickListener

            }

            if (password.length < 6) {
                toast("Password required at least 6 characters")
                return@setOnClickListener
            }
            register(username, email, password)
        }

    }

    fun register(username: String, email: String, password: String) {
        if (uri == null) return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                H.l("User Create Successfully and user id of ${it.result?.user?.uid}")
                userProfileUpload()

            }

            .addOnFailureListener {
                H.l("User Create Fail")
            }

    }

    private fun storeUserDataToDb(profileImageUrl: String) {
        val username = register_username.text.toString()
        val uid = FirebaseAuth.getInstance().uid.toString()
        val user = User(uid, username, profileImageUrl)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this@RegisterActivity, LastChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                H.l("User inserting fail due to ${it.message}")
            }
    }

    private fun userProfileUpload() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference(("images/$filename"))
        ref.putFile(uri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    H.l("Download image url is $it")
                    storeUserDataToDb("$it")
                }
            }
            .addOnFailureListener {
                H.l("Profile image upload fail ${it.message}")
            }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Deny")
                } else {
                    startImagePick()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startImagePick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Carefully"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data
            val instr: InputStream = contentResolver.openInputStream(uri!!)!!
            val bitmap = BitmapFactory.decodeStream(instr)
            profileImage.imageBitmap = bitmap
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
