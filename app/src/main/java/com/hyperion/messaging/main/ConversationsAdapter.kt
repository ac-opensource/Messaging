package com.hyperion.messaging.main

import android.view.View
import com.airbnb.epoxy.EpoxyAdapter


/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

class ConversationsAdapter(var onClickListener: View.OnClickListener) : EpoxyAdapter() {
//    fun addConversations(messages: Collection<Thread>) {
//        for (match in messages) {
//            addModel(MatchModel(match, onClickListener))
//        }
//    }
}
//
//class MatchModel(private val match: Match, private val onClickListener: View.OnClickListener) : EpoxyModel<MatchView>() {
//
//    init {
//        id(match.id)
//    }
//
//    @LayoutRes
//    public override fun getDefaultLayout(): Int {
//        return R.layout.match_model
//    }
//
//    override fun bind(matchView: MatchView?) {
//
//        matchView?.data = match
//        matchView?.title = match.title
//        matchView?.date = "12/12/2016"
//        matchView?.distance = "12km"
//        matchView?.setOnClickListener(onClickListener)
//    }
//
//    override fun unbind(matchView: MatchView?) {
//        super.unbind(matchView)
//        matchView?.setOnClickListener(null)
//    }
//}