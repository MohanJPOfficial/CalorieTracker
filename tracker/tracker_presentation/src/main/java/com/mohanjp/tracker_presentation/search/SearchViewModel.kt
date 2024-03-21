package com.mohanjp.tracker_presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.R
import com.mohanjp.core.domain.usecase.FilterOutDigits
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core.util.UiText
import com.mohanjp.tracker_domain.model.MealType
import com.mohanjp.tracker_domain.model.TrackableFood
import com.mohanjp.tracker_domain.usecase.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
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
            UiAction.OnSearch -> {
                executeSearch()
            }

            is UiAction.OnQueryChange -> {
                _uiState.update {
                    it.copy(
                        query = uiAction.query
                    )
                }
            }

            is UiAction.OnToggleTrackableFood -> {
                _uiState.update { uiState ->
                    uiState.copy(
                        trackableFoodUiStates = uiState.trackableFoodUiStates.map {
                            if(it.trackableFood == uiAction.food) {
                                it.copy(
                                    isExpanded = !it.isExpanded
                                )
                            } else it
                        }
                    )
                }
            }

            is UiAction.OnAmountForFoodChange -> {
                _uiState.update { uiState ->
                    uiState.copy(
                        trackableFoodUiStates = uiState.trackableFoodUiStates.map {
                            if(it.trackableFood == uiAction.food) {
                                it.copy(
                                    amount = filterOutDigits(uiAction.amount)
                                )
                            } else it
                        }
                    )
                }
            }

            is UiAction.OnSearchFocusChange -> {
                _uiState.update {
                    it.copy(
                        isHintVisible = !uiAction.isFocused && it.query.isBlank()
                    )
                }
            }

            is UiAction.OnTrackFoodClick -> {
                trackFood(uiAction)
            }
        }
    }

    private fun executeSearch() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isSearching = true,
                trackableFoodUiStates = emptyList(),
            )
        }

        trackerUseCases
            .searchFood(uiState.value.query)
            .onSuccess { foods ->
                _uiState.update { uiState ->
                    uiState.copy(
                        trackableFoodUiStates = foods.map {
                            TrackableFoodUiState(it)
                        },
                        isSearching = false,
                        query = ""
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(
                        isSearching = false
                    )
                }

                _uiEvent.send(
                    UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_something_went_wrong))
                )
            }
    }

    private fun trackFood(uiAction: UiAction.OnTrackFoodClick) = viewModelScope.launch {
        val trackableUiState = uiState.value.trackableFoodUiStates.find { it.trackableFood == uiAction.trackableFood }

        trackerUseCases.trackFood(
            food = trackableUiState?.trackableFood ?: return@launch,
            amount = trackableUiState.amount.toIntOrNull() ?: return@launch,
            mealType = uiAction.mealType,
            date = uiAction.date
        )

        _uiEvent.send(UiEvent.NavigateUp)
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data class OnQueryChange(val query: String): UiAction
        data object OnSearch: UiAction
        data class OnToggleTrackableFood(val food: TrackableFood): UiAction
        data class OnAmountForFoodChange(
            val food: TrackableFood,
            val amount: String
        ): UiAction
        data class OnTrackFoodClick(
            val trackableFood: TrackableFood,
            val mealType: MealType,
            val date: LocalDate
        ): UiAction
        data class OnSearchFocusChange(val isFocused: Boolean): UiAction
    }

    data class UiState(
        val query: String = "",
        val isHintVisible: Boolean = false,
        val isSearching: Boolean = false,
        val trackableFoodUiStates: List<TrackableFoodUiState> = emptyList()
    )
}