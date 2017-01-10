package com.hyperion.messaging.data

import android.net.Uri

/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 10/01/2017
 */

data class Conversation(val name: String = "",
                        val threadId: Int = -1,
                        val contactLookupUri: Uri? = null,
                        val address: String = "",
                        val snippet: String = "",
                        val date: Long = 0,
                        val isRead: Boolean = false)