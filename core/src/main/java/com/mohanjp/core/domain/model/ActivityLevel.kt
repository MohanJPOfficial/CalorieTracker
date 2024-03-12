package com.mohanjp.core.domain.model

enum class ActivityLevel(val activity: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    companion object {
        fun fromString(activity: String?): ActivityLevel {
            return entries.firstOrNull { it.activity == activity } ?: MEDIUM
        }
    }
}