package com.hyperion.messaging.main

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.hyperion.messaging.R
import com.hyperion.messaging.data.Conversation
import kotlinx.android.synthetic.main.conversation_list_view_item.view.*


/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

class ConversationsAdapter(var onClickListener: View.OnClickListener) : EpoxyAdapter() {
    fun addConversations(conversations: List<Conversation>) {
        for (conversation in conversations) {
            addModel(ConversationModel(conversation, onClickListener))
        }
    }
    fun addConversation(conversation: Conversation) {
        addModel(ConversationModel(conversation, onClickListener))
    }
}

class ConversationModel(private val conversation: Conversation, private val onClickListener: View.OnClickListener) : EpoxyModel<ConversationView>() {
    override fun getDefaultLayout(): Int {
        return R.layout.conversation_model
    }

    override fun bind(convesationView: ConversationView?) {
        convesationView?.data = conversation
    }

}

class ConversationView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var data: Conversation? = null
        set(value) {
            field = value
            contactImage = value?.contactLookupUri
            conversationName = value?.name
            snippet = value?.snippet
            date = value?.date.toString()

        }

    var contactImage: Uri? = null
        set (value) {
            field = value
            value ?: return contactAvatar.setImageResource(R.drawable.ic_person_white_24dp)
            val photoIS = ContactsContract.Contacts.openContactPhotoInputStream(context.contentResolver, value)
            photoIS ?: return
            contactAvatar.setImageBitmap(BitmapFactory.decodeStream(photoIS))
        }

    var conversationName: String? = null
        set (value) {
            field = value
            value ?: return
            if (value == "") {
                conversationNameTextView.text = "[DRAFT]"
            } else {
                conversationNameTextView.text = value
            }
        }

    var snippet: String? = null
        set (value) {
            field = value
            value ?: return
            snippetTextView.text = value
        }

    var date: String? = null
        set(value) {
            field = value
            value ?: return
            dateTextView.text = value
        }

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.conversation_list_view_item, this)
    }

    fun clear() {
    }
}