package com.mohanjp.onboarding_presentation.nutrientgoal

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
fun NutrientGoalScreen(
    onNavigate: () -> Unit,
    onUiAction: (NutrientGoalUiAction) -> Unit,
    uiState: NutrientGoalState,
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
                text = stringResource(id = R.string.what_are_your_nutrient_goals),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            UnitTextField(
                value = uiState.carbsRatio,
                onValueChange = { ratio ->
                    onUiAction(NutrientGoalUiAction.OnCarbRatioEnter(ratio))
                },
                unit = stringResource(id = R.string.percent_carbs)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            UnitTextField(
                value = uiState.proteinRatio,
                onValueChange = { ratio ->
                    onUiAction(NutrientGoalUiAction.OnProteinRatioEnter(ratio))
                },
                unit = stringResource(id = R.string.percent_proteins)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            UnitTextField(
                value = uiState.fatRatio,
                onValueChange = { ratio ->
                    onUiAction(NutrientGoalUiAction.OnFatRatioEnter(ratio))
                },
                unit = stringResource(id = R.string.percent_fats)
            )
        }

        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = { onUiAction(NutrientGoalUiAction.OnNextClick) },
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
private fun NutrientGoalScreenPreview() {
    NutrientGoalScreen(
        onNavigate = {},
        onUiAction = {},
        uiState = NutrientGoalState(),
        uiEvent = flow {  },
        showSnackBar = {}
    )
}