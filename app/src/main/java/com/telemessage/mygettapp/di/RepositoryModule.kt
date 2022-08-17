package com.telemessage.mygettapp.di

import com.telemessage.mygettapp.repository.TaxiRepository
import com.telemessage.mygettapp.repository.TaxiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repository: TaxiRepositoryImpl): TaxiRepository
}