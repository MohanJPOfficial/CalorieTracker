package com.mohanjp.tracker_presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.core.R
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.tracker_domain.model.MealType
import com.mohanjp.tracker_presentation.search.components.SearchTextField
import com.mohanjp.tracker_presentation.search.components.TrackableFoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun SearchScreen(
    onNavigateUp: () -> Unit,
    uiAction: (SearchViewModel.UiAction) -> Unit,
    uiState: SearchViewModel.UiState,
    uiEvent: Flow<UiEvent>,
    showSnackBar: suspend (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    showSnackBar(event.message.asString(context))
                    keyboardController?.hide()
                }
                UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, uiState.mealName),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        SearchTextField(
            text = uiState.query,
            onValueChange = { uiAction(SearchViewModel.UiAction.OnQueryChange(it)) },
            onSearch = {
                keyboardController?.hide()
                uiAction(SearchViewModel.UiAction.OnSearch)
            },
            onFocusChanged = { uiAction(SearchViewModel.UiAction.OnSearchFocusChange(it.isFocused)) },
            shouldShowHint = uiState.isHintVisible
        )

        Spacer(modifier = Modifier.height(spacing.spaceMedium))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.trackableFoodUiStates) { trackableFoodUiState ->

                TrackableFoodItem(
                    trackableFoodUiState = trackableFoodUiState,
                    onClick = {
                        uiAction(SearchViewModel.UiAction.OnToggleTrackableFood(trackableFoodUiState.trackableFood))
                    },
                    onAmountChange = { amount ->
                        uiAction(SearchViewModel.UiAction.OnAmountForFoodChange(trackableFoodUiState.trackableFood, amount))
                    },
                    onTrack = {
                        keyboardController?.hide()
                        uiAction(SearchViewModel.UiAction.OnTrackFoodClick(
                            trackableFood = trackableFoodUiState.trackableFood,
                            mealType = MealType.fromString(uiState.mealName),
                            date = uiState.date
                        ))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isSearching -> CircularProgressIndicator()
            uiState.trackableFoodUiStates.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        onNavigateUp = { },
        uiAction = { },
        uiState = SearchViewModel.UiState(),
        uiEvent = flow { },
        showSnackBar = { }
    )
}