package com.mohanjp.core.domain.model

enum class Gender(val gender: String) {
    MALE("male"),
    FEMALE("female");

    companion object {
        fun fromString(gender: String?): Gender {
            return entries.firstOrNull { it.gender == gender } ?: MALE
        }
    }
}