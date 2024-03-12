package com.mohanjp.core.navigation

enum class Screen(val route: String) {

    /**
     * onboard
     */
    WELCOME("welcome"),
    AGE("age"),
    GENDER("gender"),
    HEIGHT("height"),
    WEIGHT("weight"),
    NUTRIENT_GOAL("nutrient_goal"),
    ACTIVITY("activity"),
    GOAL("goal"),

    /**
     * tracker
     */
    TRACKER_OVERVIEW("tracker_overview"),
    SEARCH("search")
}