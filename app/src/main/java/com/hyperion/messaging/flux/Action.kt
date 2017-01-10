package com.hyperion.messaging.flux

/**
 * @author A-Ar Concepcion
 */
class Action(
        val type: String,
        val data: Any?
) {
    companion object {
        val ACTION_NO_ACTION = "ACTION_NO_ACTION"
    }
}
