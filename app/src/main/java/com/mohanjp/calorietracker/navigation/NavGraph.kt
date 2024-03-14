package com.mohanjp.calorietracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohanjp.core.navigation.Screen
import com.mohanjp.onboarding_presentation.gender.GenderScreen
import com.mohanjp.onboarding_presentation.gender.GenderViewModel
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
            route = Screen.GENDER.route
        ) {
            val viewModel: GenderViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            GenderScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(
            route = Screen.AGE.route
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