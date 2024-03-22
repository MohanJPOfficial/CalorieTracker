package com.mohanjp.onboarding_presentation.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.domain.model.Gender
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
class GenderViewModel @Inject constructor(
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
            is UiAction.OnGenderClick -> {
                onGenderClick(uiAction.gender)
            }
            UiAction.OnNextClick -> {
                onNextClick()
            }
        }
    }

    private fun onGenderClick(gender: Gender) {
        _uiState.update {
            it.copy(
                selectedGender = gender
            )
        }
    }

    private fun onNextClick() = viewModelScope.launch {
        preferences.saveGender(uiState.value.selectedGender)
        sendEvent(UiEvent.Navigate(Screen.Age))
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnGenderClick(val gender: Gender): UiAction
        data object OnNextClick: UiAction
    }

    data class UiState(
        val selectedGender: Gender = Gender.MALE
    )
}