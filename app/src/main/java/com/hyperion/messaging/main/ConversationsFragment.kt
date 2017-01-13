package com.hyperion.messaging.main

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.crash.FirebaseCrash
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.flux.action.SmsActionCreator
import com.hyperion.messaging.flux.store.SmsStore
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.fragment_message_list.*
import javax.inject.Inject


/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 10/01/2017
 */
class ConversationsFragment : Fragment() {

    @Inject lateinit var smsActionCreator: SmsActionCreator
    @Inject lateinit var smsStore: SmsStore

    private val TAG: String = ConversationsFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_message_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val conversationAdapter = ConversationsAdapter(View.OnClickListener {
            val data = (it as ConversationView).data
            MessageActivity.startActivity(context, data!!)
        })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        RxPermissions(activity).request(Manifest.permission.READ_SMS)
                .flatMap { smsStore.observableWithFilter(SmsActionCreator.ACTION_GET_CONVERSATIONS_SUCESS) }
                .map { it.conversationList()?.conversations }
                .subscribe({
                    it ?: return@subscribe
                    conversationAdapter.addConversations(it)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = conversationAdapter
                    Log.d("Heyyy", it.toString())
                }, {
                    FirebaseCrash.logcat(Log.ERROR, TAG, "SMS Error Caught")
                    FirebaseCrash.report(it)
                })
        smsActionCreator.getConversations()
    }
}