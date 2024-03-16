package com.mohanjp.tracker_data.di

import com.mohanjp.tracker_data.repository.TrackerRepositoryImpl
import com.mohanjp.tracker_domain.repository.TrackerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractTrackerDataModule {

    @Binds
    @Singleton
    fun bindTrackerRepository(
        trackerRepository: TrackerRepositoryImpl
    ): TrackerRepository
}