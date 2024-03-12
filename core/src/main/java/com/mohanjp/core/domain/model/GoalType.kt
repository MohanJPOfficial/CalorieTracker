package com.mohanjp.core.domain.model

enum class GoalType(val goal: String) {
    LOSE_WEIGHT("lose_weight"),
    KEEP_WEIGHT("keep_weight"),
    GAIN_WEIGHT("gain_weight");

    companion object {
        fun fromString(goal: String?): GoalType {
            return entries.firstOrNull { it.goal == goal } ?: KEEP_WEIGHT
        }
    }
}