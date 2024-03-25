package com.mohanjp.onboarding_presentation.activity

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.core.R
import com.mohanjp.core.domain.model.ActivityLevel
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.onboarding_presentation.components.ActionButton
import com.mohanjp.onboarding_presentation.components.SelectableButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun ActivityScreen(
    onNavigate: () -> Unit,
    onUiAction: (ActivityViewModel.UiAction) -> Unit,
    uiState: ActivityViewModel.UiState,
    uiEvent: Flow<UiEvent>
) {
    val spacing = LocalSpacing.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_activity_level),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Row {
                SelectableButton(
                    text = stringResource(id = R.string.low),
                    isSelected = uiState.selectedActivityLevel == ActivityLevel.LOW,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.background,
                    onClick = {
                        onUiAction(ActivityViewModel.UiAction.OnActivityLevelSelect(ActivityLevel.LOW))
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    text = stringResource(id = R.string.medium),
                    isSelected = uiState.selectedActivityLevel == ActivityLevel.MEDIUM,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.background,
                    onClick = {
                        onUiAction(ActivityViewModel.UiAction.OnActivityLevelSelect(ActivityLevel.MEDIUM))
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(spacing.spaceMedium))

                SelectableButton(
                    text = stringResource(id = R.string.high),
                    isSelected = uiState.selectedActivityLevel == ActivityLevel.HIGH,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.background,
                    onClick = {
                        onUiAction(ActivityViewModel.UiAction.OnActivityLevelSelect(ActivityLevel.HIGH))
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }

        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = { onUiAction(ActivityViewModel.UiAction.OnNextClick) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ActivityScreenPreview() {
    ActivityScreen(
        onNavigate = {},
        onUiAction = {},
        uiState = ActivityViewModel.UiState(selectedActivityLevel = ActivityLevel.MEDIUM),
        uiEvent = flow {  }
    )
}