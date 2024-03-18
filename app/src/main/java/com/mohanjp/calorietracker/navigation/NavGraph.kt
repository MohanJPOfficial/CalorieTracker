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
import com.mohanjp.onboarding_presentation.activity.ActivityScreen
import com.mohanjp.onboarding_presentation.activity.ActivityViewModel
import com.mohanjp.onboarding_presentation.age.AgeScreen
import com.mohanjp.onboarding_presentation.age.AgeViewModel
import com.mohanjp.onboarding_presentation.gender.GenderScreen
import com.mohanjp.onboarding_presentation.gender.GenderViewModel
import com.mohanjp.onboarding_presentation.goal.GoalScreen
import com.mohanjp.onboarding_presentation.goal.GoalViewModel
import com.mohanjp.onboarding_presentation.height.HeightScreen
import com.mohanjp.onboarding_presentation.height.HeightViewModel
import com.mohanjp.onboarding_presentation.nutrientgoal.NutrientGoalScreen
import com.mohanjp.onboarding_presentation.nutrientgoal.NutrientGoalViewModel
import com.mohanjp.onboarding_presentation.weight.WeightScreen
import com.mohanjp.onboarding_presentation.weight.WeightViewModel
import com.mohanjp.onboarding_presentation.welcome.WelcomeScreen
import com.mohanjp.tracker_presentation.trackeroverview.TrackerOverviewScreen
import com.mohanjp.tracker_presentation.trackeroverview.TrackerOverviewViewModel

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
            val viewModel: ActivityViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ActivityScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(
            route = Screen.GOAL.route
        ) {
            val viewModel: GoalViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            GoalScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(
            route = Screen.NUTRIENT_GOAL.route
        ) {
            val viewModel: NutrientGoalViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            NutrientGoalScreen(
                onNavigate = navHostController::navigate,
                onUiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent,
                showSnackBar = snackBarHostState::showSnackbar
            )
        }
        composable(
            route = Screen.TRACKER_OVERVIEW.route
        ) {
            val viewModel: TrackerOverviewViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TrackerOverviewScreen(
                onNavigate = navHostController::navigate,
                uiAction = viewModel.uiAction,
                uiState = uiState,
                uiEvent = viewModel.uiEvent
            )
        }
        composable(
            route = Screen.SEARCH.route
        ) {

        }
    }
}