package com.mohanjp.onboarding_domain.usecase

import com.mohanjp.core.R
import com.mohanjp.core.util.UiText

class ValidateNutrients {

    operator fun invoke(
        carbsRatioText: String,
        proteinRatioText: String,
        fatRatioText: String
    ): Result {
        val carbsRatio = carbsRatioText.toIntOrNull()
        val proteinRatio = proteinRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()

        return when {
            carbsRatio == null || proteinRatio == null || fatRatio == null -> {
                Result.Error(
                    message = UiText.StringResource(R.string.error_invalid_values)
                )
            }
            carbsRatio + proteinRatio + fatRatio != 100 -> {
                Result.Error(
                    message = UiText.StringResource(R.string.error_not_100_percent)
                )
            }
            else -> {
                Result.Success(
                    carbsRatio = carbsRatio / 100f,
                    proteinRatio = proteinRatio / 100f,
                    fatRatio = fatRatio / 100f,
                )
            }
        }
    }

    sealed interface Result {
        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ): Result

        data class Error(
            val message: UiText
        ): Result
    }
}