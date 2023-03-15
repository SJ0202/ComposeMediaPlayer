package com.seongju.composemediaplayer.di

import android.app.Application
import com.seongju.composemediaplayer.common.service.ServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideServiceManager(application: Application): ServiceManager {
        return ServiceManager(context = application).also {
            it.startPlayerService()
        }
    }

}