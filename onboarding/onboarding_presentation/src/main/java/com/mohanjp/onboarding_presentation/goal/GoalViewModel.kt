package com.mohanjp.onboarding_presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.domain.model.GoalType
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
class GoalViewModel @Inject constructor(
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
            is UiAction.OnGoalTypeSelect -> {
                updateGoalType(uiAction.goalType)
            }
            UiAction.OnNextClick -> {
                onNextClick()
            }
        }
    }

    private fun updateGoalType(goalType: GoalType) {
        _uiState.update {
            it.copy(
                selectedGoal = goalType
            )
        }
    }

    private fun onNextClick() = viewModelScope.launch {
        preferences.saveGoalType(uiState.value.selectedGoal)
        sendEvent(UiEvent.Navigate(Screen.NUTRIENT_GOAL))
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnGoalTypeSelect(val goalType: GoalType): UiAction
        data object OnNextClick: UiAction
    }

    data class UiState(
        val selectedGoal: GoalType = GoalType.KEEP_WEIGHT
    )
}