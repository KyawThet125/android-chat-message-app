package ktk.cumtla.my_chat.libby

import android.util.Log
import ktk.cumtla.my_chat.modals.User

class H {
    companion object {
        var user : User? = null
        fun l(msg: String) {
            Log.d("my_msg", msg)
        }
    }
}