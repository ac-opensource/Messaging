package com.hyperion.messaging.flux

/**
 * @author A-Ar Concepcion
 */
interface AliveUiThread {

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    fun runOnUiThreadIfAlive(runnable: Runnable)

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    fun runOnUiThreadIfAlive(runnable: Runnable, delayMillis: Long)
}
