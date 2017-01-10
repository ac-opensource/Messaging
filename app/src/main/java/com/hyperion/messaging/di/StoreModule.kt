package com.hyperion.messaging.di

import com.hyperion.messaging.flux.Dispatcher
import com.hyperion.messaging.flux.store.UserStore
import dagger.Module
import dagger.Provides
import java.util.*
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
    fun providesDispatcher(userStore: UserStore): Dispatcher {
        return Dispatcher(
                Arrays.asList(userStore))
    }

    @Singleton
    @Provides
    fun providesUserStore(): UserStore {
        return UserStore()
    }
}
