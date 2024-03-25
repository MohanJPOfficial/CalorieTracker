package com.mohanjp.tracker_presentation.trackeroverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.core.R
import com.mohanjp.tracker_presentation.trackeroverview.components.AddButton
import com.mohanjp.tracker_presentation.trackeroverview.components.DaySelector
import com.mohanjp.tracker_presentation.trackeroverview.components.ExpandableMeal
import com.mohanjp.tracker_presentation.trackeroverview.components.NutrientsHeader
import com.mohanjp.tracker_presentation.trackeroverview.components.TrackedFoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun TrackerOverviewScreen(
    onNavigate: (
        mealName: String,
        dayOfYear: Int,
        month: Int,
        year: Int
    ) -> Unit,
    uiAction: (TrackerOverviewViewModel.UiAction) -> Unit,
    uiState: TrackerOverviewViewModel.UiState,
    uiEvent: Flow<TrackerOverviewViewModel.UiEvent>
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is TrackerOverviewViewModel.UiEvent.Navigate -> {
                    onNavigate(
                        event.mealName,
                        event.dayOfMonth,
                        event.month,
                        event.year
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        item { 
            NutrientsHeader(state = uiState)
            
            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            DaySelector(
                date = uiState.readableDate.asString(context),
                onPreviousDayClick = { uiAction(TrackerOverviewViewModel.UiAction.OnPreviousDayClick) },
                onNextDayClick = { uiAction(TrackerOverviewViewModel.UiAction.OnNextDayClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        items(uiState.mealUiStates) { meal ->
            ExpandableMeal(
                modifier = Modifier.fillMaxWidth(),
                mealUiState = meal,
                onToggleClick = { uiAction(TrackerOverviewViewModel.UiAction.OnToggleMealClick(meal)) },
                content = {
                      Column(
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(horizontal = spacing.spaceSmall)
                      ) {
                          uiState.trackedFoods.forEach { food ->
                              TrackedFoodItem(
                                  trackedFood = food,
                                  onDeleteClick = {
                                      uiAction(TrackerOverviewViewModel.UiAction.OnDeleteTrackedFoodClick(food))
                                  }
                              )

                              Spacer(modifier = Modifier.height(spacing.spaceMedium))
                          }
                          AddButton(
                              text = stringResource(
                                  id = R.string.add_meal,
                                  meal.name.asString(context)
                              ),
                              onClick = {
                                  uiAction(TrackerOverviewViewModel.UiAction.OnAddFoodClick(meal))
                              },
                              modifier = Modifier.fillMaxWidth()
                          )
                      }
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TrackerOverScreenPreview() {
    TrackerOverviewScreen(
        onNavigate = {_,_,_,_ ->},
        uiAction = {},
        uiState = TrackerOverviewViewModel.UiState(),
        uiEvent = flow { }
    )
}