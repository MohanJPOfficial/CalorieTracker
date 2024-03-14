package com.mohanjp.calorietracker.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohanjp.core.navigation.Screen
import com.mohanjp.onboarding_presentation.age.AgeScreen
import com.mohanjp.onboarding_presentation.age.AgeViewModel
import com.mohanjp.onboarding_presentation.gender.GenderScreen
import com.mohanjp.onboarding_presentation.gender.GenderViewModel
import com.mohanjp.onboarding_presentation.height.HeightScreen
import com.mohanjp.onboarding_presentation.height.HeightViewModel
import com.mohanjp.onboarding_presentation.weight.WeightScreen
import com.mohanjp.onboarding_presentation.weight.WeightViewModel
import com.mohanjp.onboarding_presentation.welcome.WelcomeScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState
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
            val viewModel: AgeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AgeScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent,
                showSnackBar = snackBarHostState::showSnackbar
            )
        }
        composable(
            route = Screen.HEIGHT.route
        ) {
            val viewModel: HeightViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HeightScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent,
                showSnackBar = snackBarHostState::showSnackbar
            )
        }
        composable(
            route = Screen.WEIGHT.route
        ) {
            val viewModel: WeightViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            WeightScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent,
                showSnackBar = snackBarHostState::showSnackbar
            )
        }
        composable(
            route = Screen.ACTIVITY.route
        ) {

        }
        composable(
            route = Screen.NUTRIENT_GOAL.route
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