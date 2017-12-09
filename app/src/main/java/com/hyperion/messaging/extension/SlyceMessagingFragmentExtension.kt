package com.hyperion.messaging.extension

import it.slyce.messaging.SlyceMessagingFragment
import kotlinx.android.synthetic.main.fragment_message_list.*

/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 18/01/2017
 */

class SlyceMessagingFragmentExtension {
    companion object {
        fun SlyceMessagingFragment.fixScrollIssue() {
            recyclerView.setHasFixedSize(true)
        }
    }
}
