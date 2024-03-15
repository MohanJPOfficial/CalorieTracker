package com.mohanjp.onboarding_presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.navigation.Screen
import com.mohanjp.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiAction: (UiAction) -> Unit

    init {
        uiAction = ::onUiAction
    }

    private fun onUiAction(uiAction: UiAction) {
        when(uiAction) {
            is UiAction.OnActivityLevelSelect -> {
                updateActivityLevel(uiAction.level)
            }
            UiAction.OnNextClick -> {
                onNextClick()
            }
        }
    }

    private fun updateActivityLevel(level: ActivityLevel) {
        _uiState.update {
            it.copy(
                selectedActivityLevel = level
            )
        }
    }

    private fun onNextClick() = viewModelScope.launch {
        preferences.saveActivityLevel(uiState.value.selectedActivityLevel)
        sendEvent(UiEvent.Navigate(Screen.GOAL))
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnActivityLevelSelect(val level: ActivityLevel): UiAction
        data object OnNextClick: UiAction
    }

    data class UiState(
        val selectedActivityLevel: ActivityLevel = ActivityLevel.MEDIUM
    )
}