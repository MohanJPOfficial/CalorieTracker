package com.mohanjp.core.navigation

sealed class Screen(open val route: String) {

    /**
     * onboard
     */
    data object Welcome: Screen("welcome")
    data object Age: Screen("age")
    data object Gender: Screen("gender")
    data object Height: Screen("height")
    data object Weight: Screen("weight")
    data object NutrientGoal: Screen("nutrient_goal")
    data object Activity: Screen("activity")
    data object Goal: Screen("goal")

    /**
     * tracker
     */
    data object TrackerOverview: Screen("tracker_overview")
    data class Search(override val route: String): Screen(route) {
        companion object {

            val route = "search/{${SearchScreenArgs.MEAL_NAME}}/{${SearchScreenArgs.DAY_OF_MONTH}}/" +
                    "{${SearchScreenArgs.MONTH}}/{${SearchScreenArgs.YEAR}}"

            fun passSearchScreenArgs(
                mealName: String,
                dayOfMonth: Int,
                month: Int,
                year: Int
            ): Screen {
                return Search(
                    "search/$mealName/$dayOfMonth/$month/$year"
                )
            }
        }
    }
}