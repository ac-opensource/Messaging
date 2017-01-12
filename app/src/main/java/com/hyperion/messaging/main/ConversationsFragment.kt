package com.hyperion.messaging.main

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyperion.messaging.R
import com.hyperion.messaging.flux.model.SmsModel
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.fragment_message_list.*
import rx.Observable



/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 10/01/2017
 */
class ConversationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_message_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val conversationAdapter = ConversationsAdapter(View.OnClickListener { })
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val smsModel = SmsModel(activity)
        RxPermissions(activity).request(Manifest.permission.READ_SMS)
                .map { smsModel.getConversationIds() }
                .flatMap { Observable.from(it) }
                .map { smsModel.fillConversationDetails(it) }
                .map { smsModel.lookForContact(it) }
                .subscribe({
                    conversationAdapter.addConversation(it)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = conversationAdapter
                    Log.d("Heyyy", it.toString())
                }, {
                    Log.e("Heyyy error", it.message, it)
                })


    }
}