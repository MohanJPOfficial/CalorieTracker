package com.mohanjp.onboarding_domain.di

import com.mohanjp.onboarding_domain.usecase.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardDomainModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase() = ValidateNutrients()
}