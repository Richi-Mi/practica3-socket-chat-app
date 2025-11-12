package com.richi_mc.whatsup.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {

    companion object {
        const val NETWORK_DATA_SOURCE = "networkDataSource"
    }

    @Provides
    @Singleton
    @Named(NETWORK_DATA_SOURCE)
    fun provideNetworkDataSource() : NetworkDataSource {
        return NetworkDataSource()
    }
}