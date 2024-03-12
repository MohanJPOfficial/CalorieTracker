package com.mohanjp.core.domain.preferences

import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.domain.model.Gender
import com.mohanjp.core.domain.model.GoalType
import com.mohanjp.core.domain.model.UserInfo

interface Preferences {
    fun saveGender(gender: Gender)
    fun saveAge(age: Int)
    fun saveWeight(weight: Float)
    fun saveHeight(height: Int)
    fun saveActivityLevel(level: ActivityLevel)
    fun saveGoalType(type: GoalType)
    fun saveCarbRatio(ratio: Float)
    fun saveProteinRatio(ratio: Float)
    fun saveFatRatio(ratio: Float)

    fun loadUserInfo(): UserInfo

    enum class Keys(val key: String) {
        GENDER("gender"),
        AGE("age"),
        WEIGHT("weight"),
        HEIGHT("height"),
        ACTIVITY_LEVEL("activity_level"),
        GOAL_TYPE("goal_type"),
        CARB_RATIO("carb_ratio"),
        PROTEIN_RATIO("protein_ratio"),
        FAT_RATIO("fat_ratio")
    }
}