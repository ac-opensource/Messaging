package com.hyperion.messaging.di

import com.hyperion.messaging.flux.Dispatcher
import com.hyperion.messaging.flux.store.SmsStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module to provide flux components. This class will contain the Dispatcher,
 * ActionCreators, and Stores.

 * @author A-Ar Concepcion
 */
@Module
class StoreModule {

    @Singleton
    @Provides
    fun providesDispatcher(smsStore: SmsStore): Dispatcher {
        return Dispatcher(listOf(smsStore))
    }

    @Singleton
    @Provides
    fun providesUserStore(): SmsStore {
        return SmsStore()
    }
}
