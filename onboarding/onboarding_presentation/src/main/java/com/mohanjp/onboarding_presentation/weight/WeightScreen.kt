package com.mohanjp.onboarding_presentation.weight

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohanjp.core.R
import com.mohanjp.core.util.UiEvent
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.onboarding_presentation.components.ActionButton
import com.mohanjp.onboarding_presentation.components.UnitTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun WeightScreen(
    onNavigate: () -> Unit,
    onUiAction: (WeightViewModel.UiAction) -> Unit,
    uiState: WeightViewModel.UiState,
    uiEvent: Flow<UiEvent>,
    showSnackBar: suspend (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate()
                is UiEvent.ShowSnackBar -> {
                    showSnackBar(event.message.asString(context))
                }
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
                text = stringResource(id = R.string.whats_your_weight),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            UnitTextField(
                value = uiState.weight,
                onValueChange = { weight ->
                    onUiAction(WeightViewModel.UiAction.OnWeightEnter(weight))
                },
                unit = stringResource(id = R.string.kg)
            )
        }

        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = { onUiAction(WeightViewModel.UiAction.OnNextClick) },
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
private fun WeightScreenPreview() {
    WeightScreen(
        onNavigate = {},
        onUiAction = {},
        uiState = WeightViewModel.UiState(weight = "80"),
        uiEvent = flow {  },
        showSnackBar = {}
    )
}