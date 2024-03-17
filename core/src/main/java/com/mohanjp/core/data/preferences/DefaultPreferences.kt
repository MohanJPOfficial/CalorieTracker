package com.mohanjp.core.data.preferences

import android.content.SharedPreferences
import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.domain.model.Gender
import com.mohanjp.core.domain.model.GoalType
import com.mohanjp.core.domain.model.UserInfo
import com.mohanjp.core.domain.preferences.Preferences
import javax.inject.Inject

class DefaultPreferences @Inject constructor(
    private val sharedPref: SharedPreferences
): Preferences {

    private val editor by lazy {
        sharedPref.edit()
    }
    override fun saveGender(gender: Gender) {
        editor.putString(Preferences.Keys.GENDER.key, gender.gender)
            .apply()
    }

    override fun saveAge(age: Int) {
        editor.putInt(Preferences.Keys.AGE.key, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        editor.putFloat(Preferences.Keys.WEIGHT.key, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        editor.putInt(Preferences.Keys.HEIGHT.key, height)
            .apply()
    }

    override fun saveActivityLevel(level: ActivityLevel) {
        editor.putString(Preferences.Keys.ACTIVITY_LEVEL.key, level.activity)
            .apply()
    }

    override fun saveGoalType(type: GoalType) {
        editor.putString(Preferences.Keys.GOAL_TYPE.key, type.goal)
            .apply()
    }

    override fun saveCarbRatio(ratio: Float) {
        editor.putFloat(Preferences.Keys.CARB_RATIO.key, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {
        editor.putFloat(Preferences.Keys.PROTEIN_RATIO.key, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {
        editor.putFloat(Preferences.Keys.FAT_RATIO.key, ratio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {
        return sharedPref.run {
            val age = getInt(Preferences.Keys.AGE.key, -1)
            val height = getInt(Preferences.Keys.HEIGHT.key, -1)
            val weight = getFloat(Preferences.Keys.WEIGHT.key, -1f)
            val gender = getString(Preferences.Keys.GENDER.key, null)
            val activityLevel = getString(Preferences.Keys.ACTIVITY_LEVEL.key, null)
            val goalType = getString(Preferences.Keys.GOAL_TYPE.key, null)
            val carbRatio = getFloat(Preferences.Keys.CARB_RATIO.key, -1f)
            val proteinRatio = getFloat(Preferences.Keys.PROTEIN_RATIO.key, -1f)
            val fatRatio = getFloat(Preferences.Keys.FAT_RATIO.key, -1f)

            UserInfo(
                gender = Gender.fromString(gender),
                age = age,
                height = height,
                weight = weight,
                activityLevel = ActivityLevel.fromString(activityLevel),
                goalType = GoalType.fromString(goalType),
                carbRatio = carbRatio,
                proteinRatio = proteinRatio,
                fatRatio = fatRatio
            )
        }
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        editor.putBoolean(Preferences.Keys.SHOULD_SHOW_ONBOARDING.key, shouldShow)
            .apply()
    }

    override fun loadShouldShowOnboarding(): Boolean {
        return sharedPref.getBoolean(
            Preferences.Keys.SHOULD_SHOW_ONBOARDING.key,
            true
        )
    }

    companion object {
        const val SHARED_PREF_NAME = "shared_pref"
    }
}