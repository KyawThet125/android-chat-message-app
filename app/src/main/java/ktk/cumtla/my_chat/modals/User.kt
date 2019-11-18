package ktk.cumtla.my_chat.modals

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val name: String, val image: String) : Parcelable{
    constructor() : this("", "", "")
}