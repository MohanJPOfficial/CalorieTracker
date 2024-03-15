package com.mohanjp.onboarding_presentation.nutrientgoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.domain.usecase.FilterOutDigits
import com.mohanjp.core.navigation.Screen
import com.mohanjp.core.util.UiEvent
import com.mohanjp.onboarding_domain.usecase.ValidateNutrients
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
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
    private val validateNutrients: ValidateNutrients
): ViewModel() {

    private val _uiState = MutableStateFlow(NutrientGoalState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NutrientGoalState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiAction: (NutrientGoalUiAction) -> Unit

    init {
        uiAction = ::onUiAction
    }

    private fun onUiAction(uiAction: NutrientGoalUiAction) {
        when(uiAction) {
            is NutrientGoalUiAction.OnCarbRatioEnter -> {
                _uiState.update {
                    it.copy(
                        carbsRatio = filterOutDigits(uiAction.ratio)
                    )
                }
            }
            is NutrientGoalUiAction.OnProteinRatioEnter -> {
                _uiState.update {
                    it.copy(
                        proteinRatio = filterOutDigits(uiAction.ratio)
                    )
                }
            }
            is NutrientGoalUiAction.OnFatRatioEnter -> {
                _uiState.update {
                    it.copy(
                        fatRatio = filterOutDigits(uiAction.ratio)
                    )
                }
            }
            NutrientGoalUiAction.OnNextClick -> {
                validateInternal()
            }
        }
    }

    private fun validateInternal() {
        val result = validateNutrients(
            carbsRatioText = uiState.value.carbsRatio,
            proteinRatioText = uiState.value.proteinRatio,
            fatRatioText = uiState.value.fatRatio
        )

        when(result) {
            is ValidateNutrients.Result.Success -> {
                with(preferences) {
                    saveCarbRatio(result.carbsRatio)
                    saveProteinRatio(result.proteinRatio)
                    saveFatRatio(result.fatRatio)
                }

                sendEvent(UiEvent.Navigate(Screen.TRACKER_OVERVIEW))
            }
            is ValidateNutrients.Result.Error -> {
                sendEvent(UiEvent.ShowSnackBar(result.message))
            }
        }
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }
}