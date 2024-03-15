package com.mohanjp.onboarding_presentation.nutrientgoal

sealed interface NutrientGoalUiAction {
    data class OnCarbRatioEnter(val ratio: String): NutrientGoalUiAction
    data class OnProteinRatioEnter(val ratio: String): NutrientGoalUiAction
    data class OnFatRatioEnter(val ratio: String): NutrientGoalUiAction
    data object OnNextClick: NutrientGoalUiAction
}