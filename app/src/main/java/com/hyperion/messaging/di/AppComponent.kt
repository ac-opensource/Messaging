package com.hyperion.messaging.di

import android.content.Context
import dagger.Component
import javax.inject.Singleton

/**
 *
 *
 * HYPERION
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 21/12/2016
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun context() : Context
}