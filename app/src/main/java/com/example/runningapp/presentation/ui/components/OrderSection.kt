package com.example.runningapp.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.runningapp.domain.util.RunsOrder


@Composable
fun OrderSection(
    runsOrder: RunsOrder,
    onOrderChange: (RunsOrder) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            DefaultRadioButton(
                modifier = Modifier.padding(5.dp),
                text = "Date",
                selected = runsOrder == RunsOrder.DATE,
                onSelected = { onOrderChange(RunsOrder.DATE) })
            DefaultRadioButton(
                modifier = Modifier.padding(5.dp),
                text = "Speed",
                selected = runsOrder == RunsOrder.SPEED,
                onSelected = { onOrderChange(RunsOrder.SPEED) })
            DefaultRadioButton(
                modifier = Modifier.padding(5.dp),
                text = "Distance",
                selected = runsOrder == RunsOrder.DISTANCE,
                onSelected = { onOrderChange(RunsOrder.DISTANCE) })
        }
            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                DefaultRadioButton(
                    modifier=Modifier.padding(5.dp),
                    text = "Calories",
                    selected = runsOrder == RunsOrder.CALORIES,
                    onSelected = { onOrderChange(RunsOrder.CALORIES) })
                DefaultRadioButton(
                    modifier=Modifier.padding(5.dp),
                    text = "Time",
                    selected = runsOrder == RunsOrder.TIME,
                    onSelected = { onOrderChange(RunsOrder.TIME) })
            }
    }
}

@Composable
private fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}