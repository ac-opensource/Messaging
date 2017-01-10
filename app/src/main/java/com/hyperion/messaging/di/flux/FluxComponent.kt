package com.hyperion.messaging.di.flux;

import com.hyperion.messaging.di.*
import com.hyperion.messaging.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ActionCreatorModule::class,
        AppModule::class,
        RestModule::class,
        ModelModule::class,
        StoreModule::class)
)
interface FluxComponent {

    fun inject(mainActivity: MainActivity)

}