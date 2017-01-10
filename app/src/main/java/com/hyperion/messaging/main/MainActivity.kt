package com.hyperion.messaging.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.flux.BaseActivity
import com.hyperion.messaging.flux.action.UserActionCreator
import com.hyperion.messaging.flux.model.SmsModel
import com.tbruyelle.rxpermissions.RxPermissions
import rx.Observable
import javax.inject.Inject


class MainActivity : BaseActivity() {
    private var n = 0
    private val latin = arrayOf("Vestibulum dignissim enim a mauris malesuada fermentum. Vivamus tristique consequat turpis, pellentesque.", "Quisque nulla leo, venenatis ut augue nec, dictum gravida nibh. Donec augue nisi, volutpat nec libero.", "Cras varius risus a magna egestas.", "Mauris tristique est eget massa mattis iaculis. Aenean sed purus tempus, vestibulum ante eget, vulputate mi. Pellentesque hendrerit luctus tempus. Cras feugiat orci.", "Morbi ullamcorper, sapien mattis viverra facilisis, nisi urna sagittis nisi, at luctus lectus elit.", "Phasellus porttitor fermentum neque. In semper, libero id mollis.", "Praesent fermentum hendrerit leo, ac rutrum ipsum vestibulum at. Curabitur pellentesque augue.", "Mauris finibus mi commodo molestie placerat. Curabitur aliquam metus vitae erat vehicula ultricies. Sed non quam nunc.", "Praesent vel velit at turpis vestibulum eleifend ac vehicula leo. Nunc lacinia tellus eget ipsum consequat fermentum. Nam purus erat, mollis sed ullamcorper nec, efficitur.", "Suspendisse volutpat enim eros, et.")

    @Inject lateinit var userActionCreator: UserActionCreator
    companion object {
        fun startActivity(context: Context) {
            val intent: Intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val smsModel = SmsModel(this)
        RxPermissions(this).request(Manifest.permission.READ_SMS)
                .map { smsModel.getConversationIds() }
                .flatMap { Observable.from(it) }
                .map { smsModel.fillConversationDetails(it) }
                .map { smsModel.lookForContact(it) }
                .subscribe({
                    Log.d("Heyyy", it.toString())
                }, {
                    Log.e("Heyyy error", it.message, it)
                })
    }
}
