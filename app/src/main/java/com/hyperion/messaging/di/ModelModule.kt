package com.hyperion.messaging.di

import android.content.Context
import com.hyperion.messaging.flux.model.SmsModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModelModule {

    @Singleton
    @Provides
    fun providesSmsModel(context: Context): SmsModel {
        return SmsModel(context)
    }
}
