package com.hyperion.messaging.di

import com.hyperion.messaging.api.RestApi
import com.hyperion.messaging.flux.model.UserModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {

    @Singleton
    @Provides
    fun providesUserModel(restApi: RestApi): UserModel {
        return UserModel(restApi)
    }
}
