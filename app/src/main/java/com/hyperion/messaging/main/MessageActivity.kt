package com.hyperion.messaging.main

import android.app.Activity
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.data.Conversation
import com.hyperion.messaging.extension.SlyceMessagingFragmentExtension.Companion.fixScrollIssue
import com.hyperion.messaging.flux.BaseActivity
import com.hyperion.messaging.flux.model.SmsModel
import com.hyperion.messaging.util.ActivityExtensions.Companion.getScaledBitMapBasedOnScreenSize
import com.hyperion.messaging.util.BlurBuilder
import it.slyce.messaging.SlyceMessagingFragment
import it.slyce.messaging.listeners.UserSendsMessageListener
import kotlinx.android.synthetic.main.activity_message.*
import java.util.*
import javax.inject.Inject




class MessageActivity: BaseActivity() {

    @Inject lateinit var smsModel: SmsModel

    companion object {
        private val EXTRA_CONVERSATION = "EXTRA_CONVERSATION"
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"

        fun getIntent(context: Context, conversation: Conversation): Intent {
            val intent: Intent = Intent(context, MessageActivity::class.java)
            return intent.putExtra(EXTRA_CONVERSATION, conversation)
        }

        fun startActivity(context: Context, conversation: Conversation) {
            context.startActivity(getIntent(context, conversation))
        }
    }

    private var page: Int = 0

    private var slyceMessagingFragment: SlyceMessagingFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_message)

        val conversation = intent.getParcelableExtra<Conversation>(EXTRA_CONVERSATION)
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
        slyceMessagingFragment = fragmentManager.findFragmentById(R.id.messaging_fragment) as SlyceMessagingFragment
        slyceMessagingFragment?.let {
            it.fixScrollIssue()
            it.setPictureButtonVisible(false)
            it.setStyle(R.style.SlyceTransparent)
            it.setOnSendMessageListener(object : UserSendsMessageListener {
                override fun onUserSendsTextMessage(text: String) {
                    sendSMS(conversation.address, text)
                }

                override fun onUserSendsMediaMessage(imageUri: Uri) {
                    Log.d("inf", "******************************** " + imageUri)
                }
            })

            it.addNewMessages(smsModel.getMessagesFromConversation(conversation!!))
            it.setLoadMoreMessagesListener {
                page += 1
                smsModel.getMessagesFromConversation(conversation, page)
            }
            it.setMoreMessagesExist(true)
        }
    }

    fun sendSMS(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        val sms = SmsManager.getDefault()
        val parts = sms.divideMessage(message)
        val messageCount = parts.size

        Log.i("Message Count", "Message Count: " + messageCount)

        val deliveryIntents = ArrayList<PendingIntent>()
        val sentIntents = ArrayList<PendingIntent>()

        val sentPI = PendingIntent.getBroadcast(this, 0, Intent(SENT), 0)
        val deliveredPI = PendingIntent.getBroadcast(this, 0, Intent(DELIVERED), 0)

        for (j in 0..messageCount - 1) {
            sentIntents.add(sentPI)
            deliveryIntents.add(deliveredPI)
        }

        // ---when the SMS has been sent---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(baseContext, "SMS sent", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(this@MessageActivity, "Generic failure", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(this@MessageActivity, "No service", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(this@MessageActivity, "Null PDU", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(this@MessageActivity, "Radio off", Toast.LENGTH_SHORT).show()
                }
            }
        }, IntentFilter(SENT))

        // ---when the SMS has been delivered---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {

                    Activity.RESULT_OK -> Toast.makeText(this@MessageActivity, "SMS delivered",
                            Toast.LENGTH_SHORT).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(this@MessageActivity, "SMS not delivered",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }, IntentFilter(DELIVERED))
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
        /* sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents); */
    }

    override fun onResume() {
        super.onResume()

    }
}
