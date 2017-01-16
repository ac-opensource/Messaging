package com.hyperion.messaging.main

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.data.Conversation
import com.hyperion.messaging.flux.BaseActivity
import com.hyperion.messaging.flux.model.SmsModel
import com.hyperion.messaging.util.ActivityExtensions.Companion.getScaledBitMapBasedOnScreenSize
import com.hyperion.messaging.util.BlurBuilder
import it.slyce.messaging.SlyceMessagingFragment
import it.slyce.messaging.listeners.UserSendsMessageListener
import kotlinx.android.synthetic.main.activity_message.*
import javax.inject.Inject


class MessageActivity: BaseActivity() {

    @Inject lateinit var smsModel: SmsModel

    companion object {
        private val EXTRA_CONVERSATION = "EXTRA_CONVERSATION"

        fun startActivity(context: Context, conversation: Conversation) {
            val intent: Intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(EXTRA_CONVERSATION, conversation)
            context.startActivity(intent)
        }
    }

    private var conversation: Conversation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_message)

        conversation = intent.getParcelableExtra<Conversation>(EXTRA_CONVERSATION)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayUseLogoEnabled(true)
            it.title = conversation?.name
        }

        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaperBitmap = (wallpaperManager.drawable as BitmapDrawable).bitmap
        backgroundBlurred.setImageBitmap(BlurBuilder.blur(this, getScaledBitMapBasedOnScreenSize(wallpaperBitmap)))

        val slyceMessagingFragment: SlyceMessagingFragment = fragmentManager.findFragmentById(R.id.messaging_fragment) as SlyceMessagingFragment
        slyceMessagingFragment.setDefaultAvatarUrl("https://scontent-lga3-1.xx.fbcdn.net/v/t1.0-9/10989174_799389040149643_722795835011402620_n.jpg?oh=bff552835c414974cc446043ac3c70ca&oe=580717A5")
        slyceMessagingFragment.setDefaultDisplayName("Matthew Page")
        slyceMessagingFragment.setDefaultUserId("uhtnaeohnuoenhaeuonthhntouaetnheuontheuo")
        slyceMessagingFragment.setStyle(R.style.SlyceTransparent)
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

    override fun onResume() {
        super.onResume()

    }
}
