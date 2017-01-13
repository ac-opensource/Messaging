package com.hyperion.messaging.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.data.Conversation
import com.hyperion.messaging.flux.BaseActivity
import com.hyperion.messaging.flux.model.SmsModel
import it.slyce.messaging.SlyceMessagingFragment
import it.slyce.messaging.listeners.UserSendsMessageListener
import it.slyce.messaging.message.Message
import it.slyce.messaging.message.MessageSource
import it.slyce.messaging.message.TextMessage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MessageActivity: BaseActivity() {
    private var n = 0

    @Inject lateinit var smsModel: SmsModel

    companion object {
        private val EXTRA_CONVERSATION = "EXTRA_CONVERSATION"

        fun startActivity(context: Context, conversation: Conversation) {
            val intent: Intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(EXTRA_CONVERSATION, conversation)
            context.startActivity(intent)
        }
    }

    private fun getRandomMessage(): Message {
        n++
        val textMessage = TextMessage()
        textMessage.text = n.toString() + "" // +  ": " + latin[(int) (Math.random() * 10)]);
        textMessage.date = Date().getTime()
        if (Math.random() > 0.5) {
            textMessage.avatarUrl = "https://lh3.googleusercontent.com/-Y86IN-vEObo/AAAAAAAAAAI/AAAAAAAKyAM/6bec6LqLXXA/s0-c-k-no-ns/photo.jpg"
            textMessage.userId = "LP"
            textMessage.source = MessageSource.EXTERNAL_USER
        } else {
            textMessage.avatarUrl = "https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5"
            textMessage.userId = "MP"
            textMessage.source = MessageSource.LOCAL_USER
        }
        return textMessage
    }

    private var conversation: Conversation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_message)

        conversation = intent.getParcelableExtra<Conversation>(EXTRA_CONVERSATION)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.title = conversation?.name

        val slyceMessagingFragment: SlyceMessagingFragment = fragmentManager.findFragmentById(R.id.messaging_fragment) as SlyceMessagingFragment
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5")
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page")
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo")

        slyceMessagingFragment.setOnSendMessageListener(object : UserSendsMessageListener {
            override fun onUserSendsTextMessage(text: String) {
                Log.d("inf", "******************************** " + text)
            }

            override fun onUserSendsMediaMessage(imageUri: Uri) {
                Log.d("inf", "******************************** " + imageUri)
            }
        })

        slyceMessagingFragment.addNewMessages(smsModel.getMessagesFromConversation(conversation!!))

        slyceMessagingFragment.setMoreMessagesExist(false)
    }
}
