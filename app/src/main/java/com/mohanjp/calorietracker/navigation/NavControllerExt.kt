package com.mohanjp.calorietracker.navigation

import androidx.navigation.NavController
import com.mohanjp.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    navigate(event.screen.route)
}