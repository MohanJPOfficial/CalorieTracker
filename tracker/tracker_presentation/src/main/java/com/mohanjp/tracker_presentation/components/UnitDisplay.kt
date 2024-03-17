package com.mohanjp.tracker_presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mohanjp.core_ui.LocalSpacing
import com.mohanjp.core.R

@Composable
fun UnitDisplay(
    amount: Int,
    unit: String,
    modifier: Modifier = Modifier,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colorScheme.onBackground,
    unitTextSize: TextUnit = 14.sp,
    unitColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
    ) {
        Text(
            text = amount.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = amountTextSize,
            color = amountColor,
            modifier = Modifier.alignByBaseline()
        )

        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))

        Text(
            text = unit,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = unitTextSize,
            color = unitColor,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UnitDisplayPreview() {

    UnitDisplay(amount = 30, unit = stringResource(R.string.kg))
}