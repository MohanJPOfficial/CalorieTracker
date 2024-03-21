package com.mohanjp.tracker_presentation.trackeroverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.navigation.Screen
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core.util.UiText
import com.mohanjp.tracker_domain.model.TrackedFood
import com.mohanjp.tracker_domain.usecase.TrackerUseCases
import com.mohanjp.tracker_presentation.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    preferences: Preferences
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

    private var getFoodsForDateJob: Job? = null

    init {
        preferences.saveShouldShowOnboarding(false)

        uiAction = ::onUiAction
    }

    private fun onUiAction(uiAction: UiAction) {
        when(uiAction) {
            is UiAction.OnAddFoodClick -> {
                navigateToSearchScreen()
            }
            is UiAction.OnDeleteTrackedFoodClick -> {
                deleteTrackedFood(uiAction.trackedFood)
            }
            is UiAction.OnToggleMealClick -> {
                toggleMealExpanded(uiAction.mealUiState)
            }
            UiAction.OnNextDayClick -> {
                updateToNextDay()
            }
            UiAction.OnPreviousDayClick -> {
                updateToPreviousDay()
            }
        }
    }

    private fun updateToNextDay() {
        _uiState.update {
            it.copy(
                date = it.date.plusDays(1),
                readableDate = DateUtil.parseDateToReadable(it.date.plusDays(1))
            )
        }

        refreshFoods()
    }

    private fun updateToPreviousDay() {
        _uiState.update {
            it.copy(
                date = it.date.minusDays(1),
                readableDate = DateUtil.parseDateToReadable(it.date.minusDays(1))
            )
        }

        refreshFoods()
    }

    private fun toggleMealExpanded(clickedMealUiState: MealUiState) {
        _uiState.update { uiState ->
            uiState.copy(
                mealUiStates = uiState.mealUiStates.map { meal ->
                    if(meal.name == clickedMealUiState.name) {
                        meal.copy(
                            isExpanded = !meal.isExpanded
                        )
                    } else meal
                }
            )
        }
    }

    private fun deleteTrackedFood(trackedFood: TrackedFood) = viewModelScope.launch {
        trackerUseCases.deleteTrackedFood(trackedFood)
        refreshFoods()
    }

    private fun navigateToSearchScreen() {
        sendEvent(
            UiEvent.Navigate(Screen.SEARCH)
        )
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()

        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(uiState.value.date)
            .onEach { foods ->

                val nutrientResult = trackerUseCases.calculateMealNutrients(foods)

                with(nutrientResult) {
                    _uiState.update { uiState ->
                        uiState.copy(
                            totalCarbs = totalCarbs,
                            totalProtein = totalProtein,
                            totalFat = totalFat,
                            totalCalories = totalCalories,
                            carbsGoal = carbsGoal,
                            proteinGoal = proteinGoal,
                            fatGoal = fatGoal,
                            caloriesGoal = caloriesGoal,
                            trackedFoods = foods,
                            mealUiStates = uiState.mealUiStates.map { meal ->
                                val nutrientsForMeal = nutrientResult.mealNutrients[meal.mealType]
                                    ?: return@map meal.copy(
                                    carbs = 0,
                                    protein = 0,
                                    fat = 0,
                                    calories = 0
                                )

                                with(nutrientsForMeal) {
                                    meal.copy(
                                        carbs = carbs,
                                        protein = protein,
                                        fat = fat,
                                        calories = calories
                                    )
                                }
                            }
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun sendEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _uiEvent.send(uiEvent)
    }

    sealed interface UiAction {
        data object OnNextDayClick: UiAction
        data object OnPreviousDayClick: UiAction
        data class OnToggleMealClick(val mealUiState: MealUiState): UiAction
        data class OnDeleteTrackedFoodClick(val trackedFood: TrackedFood): UiAction
        data class OnAddFoodClick(val mealUiState: MealUiState): UiAction
    }

    data class UiState(
        val totalCarbs: Int = 0,
        val totalProtein: Int = 0,
        val totalFat: Int = 0,
        val totalCalories: Int = 0,
        val carbsGoal: Int = 0,
        val proteinGoal: Int = 0,
        val fatGoal: Int = 0,
        val caloriesGoal: Int = 0,
        val date: LocalDate = LocalDate.now(),
        val trackedFoods: List<TrackedFood> = emptyList(),
        val mealUiStates: List<MealUiState> = defaultMealUiStates,
        val readableDate: UiText = DateUtil.parseDateToReadable(LocalDate.now())
    )
}