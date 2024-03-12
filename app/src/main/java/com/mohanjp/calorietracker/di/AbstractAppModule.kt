package com.mohanjp.calorietracker.di

import com.mohanjp.core.data.preferences.DefaultPreferences
import com.mohanjp.core.domain.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractAppModule {

    @Binds
    @Singleton
    fun bindPreferences(
        defaultPreferences: DefaultPreferences
    ): Preferences
}