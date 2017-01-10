package com.hyperion.messaging.flux.model

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Telephony.TextBasedSmsColumns.*
import android.text.TextUtils
import com.hyperion.messaging.data.Conversation


/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 10/01/2017
 */
class SmsModel(val context: Context) {

    var SMS_CONVERSATIONS = Uri.parse("content://sms/conversations/")!!
    var SMS_INBOX = Uri.parse("content://sms/inbox")!!

    fun getConversationIds(): Set<Conversation> {
        val mCursor = context.contentResolver.query(SMS_CONVERSATIONS, null, null, null, "date desc")
        val count = arrayOfNulls<String>(mCursor.count)
        val snippet = arrayOfNulls<String>(mCursor.count)
        val thread_id = arrayOfNulls<Int>(mCursor.count)

        var conversations = mutableSetOf<Conversation>()

        if (!mCursor.moveToFirst()) return emptySet()
        for (i in 0..mCursor.count - 1) {
            count[i] = mCursor.getString(mCursor.getColumnIndexOrThrow("msg_count"))
            thread_id[i] = mCursor.getInt(mCursor.getColumnIndexOrThrow("thread_id"))
            snippet[i] = mCursor.getString(mCursor.getColumnIndexOrThrow("snippet"))
            conversations.add(Conversation(threadId = thread_id[i]!!, snippet = snippet[i]!!))
            mCursor.moveToNext()
        }
        mCursor.close()

        return conversations
//
//        val conversationIds = HashSet<Int>()
//        val numbers = "+xxxxxxxxxx,+yyyyyyyyyy"
//
//        val PROJECTION = arrayOf(Sms._ID, Sms.THREAD_ID)
//        val SELECTION = Sms.ADDRESS + " IN (" + numbers.replace("[^,]+".toRegex(), "?") + ")"
//        val selectionArgs = numbers.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//        val cursor = context.contentResolver.query(Sms.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null)
//        val threadColumn = cursor.getColumnIndexOrThrow(Sms.THREAD_ID)
//
//        while (cursor.moveToNext())
//            conversationIds.add(cursor.getInt(threadColumn))
//        cursor.close()
//
//        return conversationIds
    }

    fun fillConversationDetails(conversation: Conversation): Conversation {
        val where = "thread_id=" + conversation.threadId
        val mCursor = context.contentResolver.query(Uri.withAppendedPath(SMS_CONVERSATIONS, conversation.threadId.toString()), null, where, null, "date desc")

        var number: String? = ""
        var date: Long = 0
        var read: Boolean = false
        if (mCursor.moveToFirst()) {
            val addressColumnIndex = mCursor.getColumnIndex(ADDRESS)
            if (addressColumnIndex != -1) {
                number = mCursor.getString(addressColumnIndex) ?: ""
            }

            date = mCursor.getLong(mCursor.getColumnIndexOrThrow(DATE))
            read = "1" == mCursor.getString(mCursor.getColumnIndexOrThrow(READ))
        }
        mCursor.close()
        return conversation.copy(address = number ?: "", date = date, isRead = read)
    }

    fun lookForContact(conversation: Conversation): Conversation {
        if (TextUtils.isEmpty(conversation.address)) return conversation
        var name: String = ""
        var contactId: Long? = null
        var lookupUri: Uri? = null

        // define the columns I want the query to return
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.LOOKUP_KEY)
        // encode the phone number and build the filter URI
        val contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(conversation.address))
        // query time
        val cursor = context.contentResolver.query(contactUri, projection, null, null, null)

        if (cursor.moveToFirst()) {

            // Get values from contacts database:
            contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID))
            val lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.LOOKUP_KEY))
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))

            // Get photo of contactId as input stream:
            lookupUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey)
        }

        cursor.close()

        return conversation.copy(name = name, contactLookupUri = lookupUri)
    }
}