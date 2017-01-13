package com.hyperion.messaging

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.support.multidex.MultiDex
import com.hyperion.messaging.di.AppModule
import com.hyperion.messaging.di.flux.DaggerFluxComponent
import com.hyperion.messaging.di.flux.FluxComponent
import com.pixplicity.easyprefs.library.Prefs

class MyApplication : Application() {

    companion object {
        lateinit var fluxComponent: FluxComponent
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the Prefs class
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        fluxComponent = DaggerFluxComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        try {
            MultiDex.install(this)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }
}

