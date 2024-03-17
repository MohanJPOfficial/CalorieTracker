package com.mohanjp.tracker_presentation.trackeroverview

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohanjp.tracker_presentation.trackeroverview.components.NutrientsHeader

@Composable
fun TrackerOverviewScreen(
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    NutrientsHeader(state = viewModel.uiState.value)
}