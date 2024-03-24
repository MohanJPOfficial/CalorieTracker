package com.mohanjp.calorietracker.acitivity

import androidx.lifecycle.ViewModel
import com.mohanjp.core.domain.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    val shouldShowOnboarding: Boolean

    init {
        shouldShowOnboarding = shouldShowOnboarding()
    }

    private fun shouldShowOnboarding(): Boolean {
        return preferences.loadShouldShowOnboarding()
    }
}