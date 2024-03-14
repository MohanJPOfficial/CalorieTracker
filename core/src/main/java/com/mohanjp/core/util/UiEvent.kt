package com.mohanjp.core.util

import com.mohanjp.core.navigation.Screen

sealed interface UiEvent {
    data class Navigate(val screen: Screen): UiEvent
    data object NavigateUp: UiEvent
    data class ShowSnackBar(val message: UiText): UiEvent
}