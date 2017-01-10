package com.hyperion.messaging.di

import android.content.Context
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.flux.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: MyApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideApplication(): MyApplication {
        return app
    }

    @Provides
    @Singleton
    fun provideUtils(context: Context): Utils {
        return Utils(context)
    }
}