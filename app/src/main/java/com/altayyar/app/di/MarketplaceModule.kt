package com.altayyar.app.di

import com.altayyar.app.data.repository.DefaultMarketplaceRepository
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MarketplaceModule {

    @Binds
    @Singleton
    abstract fun bindMarketplaceRepository(
        repository: DefaultMarketplaceRepository
    ): MarketplaceRepository
}
