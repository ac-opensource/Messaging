package com.hyperion.messaging.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import com.hyperion.messaging.MyApplication

import com.hyperion.messaging.R
import com.hyperion.messaging.flux.model.SmsModel
import com.hyperion.messaging.main.MessageActivity
import com.hyperion.messaging.util.Constants
import javax.inject.Inject
import android.R.id.message
import android.content.ContentValues



class SmsReceiver : BroadcastReceiver() {


    private val TAG = SmsReceiver::class.java.simpleName
    private var bundle: Bundle? = null
    private var currentSMS: SmsMessage? = null
    private val mNotificationId = 101

    @Inject lateinit var smsModel: SmsModel

    override fun onReceive(context: Context, intent: Intent) {
        MyApplication.fluxComponent.inject(this)
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        intent.extras?.let { bundle ->
            (bundle.get("pdus") as Array<*>).let { pduObject ->
                pduObject.forEach {
                    val currentSMS = getIncomingMessage(it!!, bundle)

                    val senderNo = currentSMS.displayOriginatingAddress
                    val message = currentSMS.displayMessageBody
                    //Log.d(TAG, "senderNum: " + senderNo + " :\n message: " + message);

                    issueNotification(context, senderNo, message)
                    saveSmsInInbox(context, currentSMS)
                }
                this.abortBroadcast()
            }
        }
    }

    private fun saveSmsInInbox(context: Context, sms: SmsMessage) {

        val values = ContentValues()
        values.put("address", sms.displayOriginatingAddress)
        values.put("body", message)
        values.put("date_sent", sms.timestampMillis)
        context.contentResolver.insert(SmsContract.ALL_SMS_URI, values)

        val serviceIntent = Intent(context, SaveSmsService::class.java)
        serviceIntent.putExtra("sender_no", sms.displayOriginatingAddress)
        serviceIntent.putExtra("message", sms.displayMessageBody)
        serviceIntent.putExtra("date", sms.timestampMillis)

        context.startService(serviceIntent)

    }

    private fun issueNotification(context: Context, senderNo: String, message: String) {

        smsModel.getConversationByPhoneNumber(context, senderNo)?.let {
            val icon = BitmapFactory.decodeResource(context.resources,
                    R.mipmap.ic_launcher)

            val mBuilder = NotificationCompat.Builder(context)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(senderNo)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setAutoCancel(true)
                    .setContentText(message)

            val resultIntent = MessageActivity.getIntent(context, smsModel.fillConversationDetails(it))
            val resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            mBuilder.setContentIntent(resultPendingIntent)

            val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(mNotificationId, mBuilder.build())
        }



    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage {
        val currentSMS: SmsMessage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray, format)
        } else {
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray)
        }
        return currentSMS
    }
}