package com.hyperion.messaging.data

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 10/01/2017
 */

data class ConversationList(val conversations: List<Conversation>)

data class Conversation(val name: String = "",
                        val threadId: Int = -1,
                        val contactLookupUri: Uri? = null,
                        val address: String = "",
                        val snippet: String = "",
                        val date: Long = 0,
                        val isRead: Boolean = false) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Conversation> = object : Parcelable.Creator<Conversation> {
            override fun createFromParcel(source: Parcel): Conversation = Conversation(source)
            override fun newArray(size: Int): Array<Conversation?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readParcelable<Uri?>(Uri::class.java.classLoader), source.readString(), source.readString(), source.readLong(), 1 == source.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeInt(threadId)
        dest?.writeParcelable(contactLookupUri, 0)
        dest?.writeString(address)
        dest?.writeString(snippet)
        dest?.writeLong(date)
        dest?.writeInt((if (isRead) 1 else 0))
    }
}