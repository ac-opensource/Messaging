package com.hyperion.messaging.flux.action

import android.support.annotation.StringDef
import com.hyperion.messaging.data.ConversationList
import com.hyperion.messaging.flux.Action
import com.hyperion.messaging.flux.Dispatcher
import com.hyperion.messaging.flux.Utils
import com.hyperion.messaging.flux.model.SmsModel
import rx.Observable

/**
 * @author A-Ar Concepcion
 * *
 * @createdAt 31/08/2016
 */
class SmsActionCreator(private val mDispatcher: Dispatcher, private val smsModel: SmsModel, private val mUtils: Utils) {

    companion object {
        const val ACTION_INITIALIZE_CONVERSATIONS = "ACTION_INITIALIZE_CONVERSATIONS"
        const val ACTION_GET_CONVERSATIONS_SUCESS = "ACTION_GET_CONVERSATIONS_SUCESS"
        const val ACTION_LOGIN_FAILED = "ACTION_LOGIN_FAILED"
    }

    @StringDef(value = *arrayOf(ACTION_INITIALIZE_CONVERSATIONS, ACTION_GET_CONVERSATIONS_SUCESS))
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class SmsAction

    init {
        preload()
    }

    fun preload() {
        mDispatcher.dispatch(Action(ACTION_INITIALIZE_CONVERSATIONS, Unit))
    }

    fun getConversations(page: Int = 0) {
        Observable.just(smsModel.getConversationIds(offset = page * 50))
                .flatMap { Observable.from(it) }
                .map { smsModel.fillConversationDetails(it) }
                .map { smsModel.lookForContact(it) }
                .toList()
                .map(::ConversationList)
                .subscribe({
                    mDispatcher.dispatch(Action(ACTION_GET_CONVERSATIONS_SUCESS, it))
                }, {
                    mDispatcher.dispatch(Action(ACTION_LOGIN_FAILED, mUtils.getError(it)))
                })

    }
}
