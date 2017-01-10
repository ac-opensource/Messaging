package com.hyperion.messaging.di

import com.hyperion.messaging.flux.Dispatcher
import com.hyperion.messaging.flux.Utils
import com.hyperion.messaging.flux.action.UserActionCreator
import com.hyperion.messaging.flux.model.UserModel
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
                                           userModel: UserModel,
                                           utils: Utils): UserActionCreator {
        return UserActionCreator(dispatcher, userModel, utils)
    }
}
