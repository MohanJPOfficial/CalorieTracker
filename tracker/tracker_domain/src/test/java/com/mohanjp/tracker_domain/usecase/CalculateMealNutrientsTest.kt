package com.mohanjp.tracker_domain.usecase

import com.google.common.truth.Truth
import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.domain.model.Gender
import com.mohanjp.core.domain.model.GoalType
import com.mohanjp.core.domain.model.UserInfo
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.tracker_domain.model.MealType
import com.mohanjp.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random


class CalculateMealNutrientsTest {

    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setup() {
        val preferences = mockk<Preferences>(relaxed = true)

        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.MALE,
            age = 20,
            weight = 80f,
            height = 190,
            activityLevel = ActivityLevel.MEDIUM,
            goalType = GoalType.GAIN_WEIGHT,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "something",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.entries.random(),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)

        val breakfastCalories = result.mealNutrients.values
            .filter { it.mealType == MealType.BREAKFAST }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter { it.mealType == MealType.BREAKFAST }
            .sumOf { it.calories }

        Truth.assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Carbs for dinner properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "something",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.entries.random(),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)

        val dinnerCarbs = result.mealNutrients.values
            .filter { it.mealType == MealType.DINNER }
            .sumOf { it.carbs }

        val expectedCarbs = trackedFoods
            .filter { it.mealType == MealType.DINNER }
            .sumOf { it.carbs }

        Truth.assertThat(dinnerCarbs).isEqualTo(expectedCarbs)
    }
}