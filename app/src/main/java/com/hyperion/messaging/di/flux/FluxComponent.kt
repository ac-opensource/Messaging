package com.hyperion.messaging.di.flux;

import com.hyperion.messaging.di.ActionCreatorModule
import com.hyperion.messaging.di.AppModule
import com.hyperion.messaging.di.ModelModule
import com.hyperion.messaging.di.StoreModule
import com.hyperion.messaging.main.ConversationsFragment
import com.hyperion.messaging.main.MainActivity
import com.hyperion.messaging.main.MessageActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ActionCreatorModule::class,
        AppModule::class,
        ModelModule::class,
        StoreModule::class)
)
interface FluxComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(messageActivity: MessageActivity)
    fun inject(conversationsFragment: ConversationsFragment)

}