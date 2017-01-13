package com.hyperion.messaging.flux.store

import com.hyperion.messaging.data.ConversationList
import com.hyperion.messaging.flux.Action
import com.hyperion.messaging.flux.AppError
import com.hyperion.messaging.flux.Store
import com.hyperion.messaging.flux.action.SmsActionCreator
import com.hyperion.messaging.flux.action.SmsActionCreator.Companion.ACTION_GET_CONVERSATIONS_SUCESS
import com.hyperion.messaging.flux.action.SmsActionCreator.Companion.ACTION_INITIALIZE_CONVERSATIONS
import rx.Observable

/**
 * @author A-Ar Concepcion
 * *
 * @createdAt 17/08/2016
 */
class SmsStore : Store<SmsStore>() {

    var conversationList: ConversationList? = null
    private var mError: AppError? = null

    @SmsActionCreator.SmsAction
    private var mAction = Action.ACTION_NO_ACTION

    fun error(): AppError? {
        return mError
    }

    @SmsActionCreator.SmsAction
    fun action(): String {
        return mAction
    }

    fun observableWithFilter(action: String): Observable<SmsStore> {
        return observable().filter { action == it.action() }
    }

    override fun onReceiveAction(action: Action) {
        when (action.type) {
            ACTION_INITIALIZE_CONVERSATIONS, ACTION_GET_CONVERSATIONS_SUCESS -> {
                updateState()
                updateConversation(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
        }
    }

    fun conversationList(): ConversationList? {
        return conversationList
    }

    private fun updateConversation(action: Action) {
        if (action.data != null && action.data is ConversationList) {
            conversationList = action.data
        }
    }

    private fun updateState() {
        mError = null
    }

    private fun updateError(action: Action) {
        if (action.data != null && action.data is AppError) {
            mError = action.data as AppError?
        }
    }

    private fun updateAction(action: Action) {
        mAction = action.type
    }
}
