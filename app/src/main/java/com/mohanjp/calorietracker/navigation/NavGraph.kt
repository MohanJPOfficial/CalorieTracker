package com.mohanjp.calorietracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohanjp.core.navigation.Screen
import com.mohanjp.onboarding_presentation.welcome.WelcomeScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.WELCOME.route
    ) {
        composable(
            route = Screen.WELCOME.route
        ) {
            WelcomeScreen(
                onNavigate = navHostController::navigate
            )
        }
        composable(
            route = Screen.AGE.route
        ) {

        }
        composable(
            route = Screen.GENDER.route
        ) {

        }
        composable(
            route = Screen.HEIGHT.route
        ) {

        }
        composable(
            route = Screen.WEIGHT.route
        ) {

        }
        composable(
            route = Screen.NUTRIENT_GOAL.route
        ) {

        }
        composable(
            route = Screen.ACTIVITY.route
        ) {

        }
        composable(
            route = Screen.GOAL.route
        ) {

        }
        composable(
            route = Screen.TRACKER_OVERVIEW.route
        ) {

        }
        composable(
            route = Screen.SEARCH.route
        ) {

        }
    }
}