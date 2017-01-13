package com.hyperion.messaging.di

import com.hyperion.messaging.flux.Dispatcher
import com.hyperion.messaging.flux.Utils
import com.hyperion.messaging.flux.action.SmsActionCreator
import com.hyperion.messaging.flux.model.SmsModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author A-Ar Concepcion
 */
@Module
class ActionCreatorModule {

    @Singleton @Provides
    fun providesUserActionCreator(dispatcher: Dispatcher,
                                           smsModel: SmsModel,
                                           utils: Utils): SmsActionCreator {
        return SmsActionCreator(dispatcher, smsModel, utils)
    }
}
