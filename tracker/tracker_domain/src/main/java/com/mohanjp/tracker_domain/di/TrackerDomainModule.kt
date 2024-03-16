package com.mohanjp.tracker_domain.di

import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.tracker_domain.repository.TrackerRepository
import com.mohanjp.tracker_domain.usecase.CalculateMealNutrients
import com.mohanjp.tracker_domain.usecase.DeleteTrackedFood
import com.mohanjp.tracker_domain.usecase.GetFoodsForDate
import com.mohanjp.tracker_domain.usecase.SearchFood
import com.mohanjp.tracker_domain.usecase.TrackFood
import com.mohanjp.tracker_domain.usecase.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }
}