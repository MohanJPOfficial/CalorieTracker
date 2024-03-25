package com.mohanjp.core.util

sealed interface UiEvent {
    data object Navigate: UiEvent
    data object NavigateUp: UiEvent
    data class ShowSnackBar(val message: UiText): UiEvent
}