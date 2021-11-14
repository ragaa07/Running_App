package com.example.runningapp.presentation.homeScreen

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.runningapp.R
import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.domain.util.RunsOrder
import com.example.runningapp.presentation.ui.components.OrderSection
import com.example.runningapp.presentation.util.RunsEvents
import com.example.runningapp.presentation.util.formatDateTime
import com.example.runningapp.presentation.util.formatTimeToStopWatchFormat

@ExperimentalAnimationApi
@Composable
fun HomeScreen(currentDate: String, viewModel: RunsViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    // val state = viewModel.state.value
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hello Ragaa ",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Image(
                painter = painterResource(id = R.drawable.icons8_waving_hand_emoji_48),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .size(28.dp),
                alignment = Alignment.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Text(
                    text = currentDate,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )
            }
            IconButton(onClick = {
                viewModel.onEvent(RunsEvents.ToggleOrderSection)
            }) {
                Icon(imageVector = Icons.Default.Sort, tint = Color.Black, contentDescription = "")
            }
        }
        AnimatedVisibility(
            visible = state.orderSection,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OrderSection(
                runsOrder = state.order,
                onOrderChange = { viewModel.onEvent(RunsEvents.Order(it)) })
        }
        if (state.runs.isNotEmpty()) {
            RunningList(runs = state.runs)
        }
    }
}

@Composable
fun RunningList(runs: List<RunsDomainModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = runs) { run ->
            RunItem(run = run)
        }
    }
}

@Composable
fun RunItem(run: RunsDomainModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), shape = RoundedCornerShape(15.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = run.timestamp.formatDateTime(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            run.img?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Run ScreenShot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(70f)
                        .padding(5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(text = "Distance", color = Color.Gray)
                    Text(
                        text = "${run.distanceInMeters / 1000} Km",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Time", color = Color.Gray)
                    Text(
                        text = formatTimeToStopWatchFormat(run.timeInMillis, true),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column() {
                    Text(text = "Speed", color = Color.Gray)
                    Text(
                        text = "${run.avgSpeedInKMH} Km/h",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Calories", color = Color.Gray)
                    Text(
                        text = "${run.burnedCalories} Kcal",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
