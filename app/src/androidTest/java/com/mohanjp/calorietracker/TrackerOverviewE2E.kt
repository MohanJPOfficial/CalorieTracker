package com.mohanjp.calorietracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.common.truth.Truth
import com.mohanjp.calorietracker.acitivity.MainActivity
import com.mohanjp.calorietracker.navigation.Screen
import com.mohanjp.calorietracker.repository.TrackerRepositoryFake
import com.mohanjp.calorietracker.ui.theme.CalorieTrackerTheme
import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.domain.model.Gender
import com.mohanjp.core.domain.model.GoalType
import com.mohanjp.core.domain.model.UserInfo
import com.mohanjp.core.domain.preferences.Preferences
import com.mohanjp.core.domain.usecase.FilterOutDigits
import com.mohanjp.tracker_domain.model.TrackableFood
import com.mohanjp.tracker_domain.usecase.CalculateMealNutrients
import com.mohanjp.tracker_domain.usecase.DeleteTrackedFood
import com.mohanjp.tracker_domain.usecase.GetFoodsForDate
import com.mohanjp.tracker_domain.usecase.SearchFood
import com.mohanjp.tracker_domain.usecase.TrackFood
import com.mohanjp.tracker_domain.usecase.TrackerUseCases
import com.mohanjp.tracker_presentation.search.SearchScreen
import com.mohanjp.tracker_presentation.search.SearchScreenArgs
import com.mohanjp.tracker_presentation.search.SearchViewModel
import com.mohanjp.tracker_presentation.trackeroverview.TrackerOverviewScreen
import com.mohanjp.tracker_presentation.trackeroverview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    /*@Inject
    lateinit var savedStateHandle: SavedStateHandle*/

    @Before
    fun setup() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.MALE,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.MEDIUM,
            goalType = GoalType.KEEP_WEIGHT,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        repositoryFake = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )

        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )

        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigits = FilterOutDigits(),
            savedStateHandle = SavedStateHandle()
        )

        composeRule.setContent {
            CalorieTrackerTheme {
                val snackBarHostState = remember {
                    SnackbarHostState()
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.TrackerOverview.route
                        ) {
                            composable(
                                route = Screen.TrackerOverview.route
                            ) {
                                val uiState by trackerOverviewViewModel.uiState.collectAsStateWithLifecycle()

                                TrackerOverviewScreen(
                                    onNavigate = { mealName, dayOfYear, month, year ->
                                        navController.navigate(
                                            Screen.Search.passSearchScreenArgs(
                                                mealName = mealName,
                                                dayOfMonth = dayOfYear,
                                                month = month,
                                                year = year
                                            ))
                                    },
                                    uiAction = trackerOverviewViewModel.uiAction,
                                    uiState = uiState,
                                    uiEvent = trackerOverviewViewModel.uiEvent
                                )
                            }
                            composable(
                                route = Screen.Search.route,
                                arguments = listOf(
                                    navArgument(SearchScreenArgs.MEAL_NAME.name) {
                                        type = NavType.StringType
                                    },
                                    navArgument(SearchScreenArgs.DAY_OF_MONTH.name) {
                                        type = NavType.IntType
                                    },
                                    navArgument(SearchScreenArgs.MONTH.name) {
                                        type = NavType.IntType
                                    },
                                    navArgument(SearchScreenArgs.YEAR.name) {
                                        type = NavType.IntType
                                    }
                                )
                            ) {

                                val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

                                SearchScreen(
                                    onNavigateUp = {
                                        navController.navigateUp()
                                    },
                                    uiAction = searchViewModel.uiAction,
                                    uiState = uiState,
                                    uiEvent = searchViewModel.uiEvent,
                                    showSnackBar = snackBarHostState::showSnackbar
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        Truth.assertThat(
            navController.currentDestination?.route?.startsWith(Screen.Search.route)
        ).isTrue()

        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")

        composeRule
            .onNodeWithContentDescription("Search…")
            .performClick()

        composeRule
            .onRoot()
            .printToLog("COMPOSE TREE")

        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toStr())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        Truth.assertThat(
            navController.currentDestination?.route?.startsWith(Screen.TrackerOverview.route)
        ).isTrue()

        composeRule
            .onAllNodesWithText(expectedCarbs.toStr())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedProtein.toStr())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedFat.toStr())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedCalories.toStr())
            .onFirst()
            .assertIsDisplayed()
    }
}