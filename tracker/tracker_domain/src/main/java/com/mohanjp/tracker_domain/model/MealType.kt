package com.mohanjp.tracker_domain.model

enum class MealType(val type: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner"),
    SNACK("snack");

    companion object {
        fun fromString(mealType: String?): MealType {
            return MealType.entries.firstOrNull { it.type == mealType } ?: BREAKFAST
        }
    }
}