package com.mohanjp.onboarding_presentation.age

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.R
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.domain.usecase.FilterOutDigits
import com.mohanjp.core.navigation.Screen
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
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
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

            is UiAction.OnAgeEnter -> {
                updateAge(uiAction.age)
            }
        }
    }

    private fun updateAge(age: String) {
        if(age.length <= 3) {
            _uiState.update {
                it.copy(
                    age = filterOutDigits(age)
                )
            }
        }
    }

    private fun onNextClick() = viewModelScope.launch {
        val ageNumber = uiState.value.age.toIntOrNull() ?: kotlin.run {
            sendEvent(UiEvent.ShowSnackBar(
                UiText.StringResource(R.string.error_age_cant_be_empty)
            ))

            return@launch
        }
        preferences.saveAge(ageNumber)
        sendEvent(UiEvent.Navigate(Screen.Height))
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnAgeEnter(val age: String): UiAction
        data object OnNextClick: UiAction
    }

    data class UiState(
        val age: String = "20"
    )
}