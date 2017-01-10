package com.hyperion.messaging.di

import com.hyperion.messaging.api.GoogleApi
import com.hyperion.messaging.api.RestApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

/**
 * HYPERION

 * @author A-Ar Andrew Concepcion
 * *
 * @since 14/12/2016
 */
@Module
class RestModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://10.100.0.91:3000")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }

    @Provides
    @Singleton
    @Named("googleMaps")
    fun provideGoogleMapsRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://maps.googleapis.com/maps/api")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

    }

    @Provides
    @Singleton
    fun provideGoogleApi(@Named("googleMaps") retrofit: Retrofit): GoogleApi {
        return retrofit.create(GoogleApi::class.java)
    }
}