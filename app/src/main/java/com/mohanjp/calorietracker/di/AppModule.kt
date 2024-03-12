package com.mohanjp.calorietracker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mohanjp.core.data.preferences.DefaultPreferences
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
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences(
            DefaultPreferences.SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }
}