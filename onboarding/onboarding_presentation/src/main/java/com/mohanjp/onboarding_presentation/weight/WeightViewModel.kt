package com.mohanjp.onboarding_presentation.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.R
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core.util.UiText
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
class WeightViewModel @Inject constructor(
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
            UiAction.OnNextClick -> {
                onNextClick()
            }

            is UiAction.OnWeightEnter -> {
                updateWeight(uiAction.weight)
            }
        }
    }

    private fun updateWeight(weight: String) {
        if(weight.length <= 5) {
            _uiState.update {
                it.copy(
                    weight = weight
                )
            }
        }
    }

    private fun onNextClick() = viewModelScope.launch {
        val weightNumber = uiState.value.weight.toFloatOrNull() ?: kotlin.run {
            sendEvent(UiEvent.ShowSnackBar(
                UiText.StringResource(R.string.error_weight_cant_be_empty)
            ))

            return@launch
        }
        preferences.saveWeight(weightNumber)
        sendEvent(UiEvent.Navigate)
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnWeightEnter(val weight: String): UiAction
        data object OnNextClick: UiAction
    }

    data class UiState(
        val weight: String = "80.0"
    )
}