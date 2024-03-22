package com.mohanjp.tracker_presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.core.R
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.tracker_presentation.search.components.SearchTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun SearchScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
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
                is UiEvent.Navigate -> onNavigate(event)
                UiEvent.NavigateUp -> onNavigateUp()
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
            onSearch = { uiAction(SearchViewModel.UiAction.OnSearch) },
            onFocusChanged = { uiAction(SearchViewModel.UiAction.OnSearchFocusChange(it.isFocused)) }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        onNavigate = { },
        onNavigateUp = { },
        uiAction = { },
        uiState = SearchViewModel.UiState(),
        uiEvent = flow { },
        showSnackBar = { }
    )
}