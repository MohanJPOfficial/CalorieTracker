package com.mohanjp.tracker_presentation.trackeroverview

import androidx.annotation.DrawableRes
import com.mohanjp.core.util.UiText
import com.mohanjp.tracker_domain.model.MealType
import com.mohanjp.core.R

data class MealUiState(
    val name: UiText,
    @DrawableRes val drawableRes: Int,
    val mealType: MealType,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val calories: Int = 0,
    val isExpanded: Boolean = false
)

val defaultMealUiStates = listOf(
    MealUiState(
        name = UiText.StringResource(R.string.breakfast),
        drawableRes = R.drawable.ic_breakfast,
        mealType = MealType.BREAKFAST
    ),
    MealUiState(
        name = UiText.StringResource(R.string.lunch),
        drawableRes = R.drawable.ic_lunch,
        mealType = MealType.LUNCH
    ),
    MealUiState(
        name = UiText.StringResource(R.string.dinner),
        drawableRes = R.drawable.ic_dinner,
        mealType = MealType.DINNER
    ),
    MealUiState(
        name = UiText.StringResource(R.string.snacks),
        drawableRes = R.drawable.ic_snack,
        mealType = MealType.SNACK
    )
)