package com.hyperion.messaging.main

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.text.Html
import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
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

    var conversationHash = mutableMapOf<Int, ConversationModel>()

    fun addConversations(conversations: List<Conversation>) {
        for (conversation in conversations) {
            val conversationModel = ConversationModel(conversation, onClickListener)
            conversationHash.put(conversation.threadId, conversationModel)
            addModel(conversationModel)
        }
    }
    fun addConversation(conversation: Conversation) {
        val conversationModel = ConversationModel(conversation, onClickListener)
        conversationHash.put(conversation.threadId, conversationModel)
        addModel(conversationModel)
    }

}

class ConversationModel(private val conversation: Conversation, private val onClickListener: View.OnClickListener) : EpoxyModel<ConversationView>() {
    override fun getDefaultLayout(): Int {
        return R.layout.conversation_model
    }

    override fun bind(conversationView: ConversationView?) {
        conversationView?.data = conversation
        conversationView?.setOnClickListener(onClickListener)
    }

}

class ConversationView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var data: Conversation? = null
        set(value) {
            field = value
            contactImage = value?.contactLookupUri
            conversationName = value?.name
            snippet = value?.snippet
            date = DateUtils.getRelativeTimeSpanString(value?.date!!, System.currentTimeMillis(), 0, FORMAT_ABBREV_RELATIVE)

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
            val textColor = if (data?.isRead!!) { "#FFFFFF" } else { "#00FFFF" }
            if (Build.VERSION.SDK_INT >= 24) {
                conversationNameTextView.text = Html.fromHtml("<font color='$textColor'>${if (value == "") "[DRAFT]" else value}</font>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                conversationNameTextView.text = Html.fromHtml("<font color='$textColor'>${if (value == "") "[DRAFT]" else value}</font>")
            }
        }

    var snippet: String? = null
        set (value) {
            field = value
            value ?: return
            snippetTextView.text = value
        }

    var date: CharSequence? = null
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