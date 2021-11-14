package com.example.runningapp.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.runningapp.presentation.ui.theme.Blue800

@Composable
fun ToggleRunButtons(
    isTracking: Boolean,
    isFirstRun: Boolean,
    timer: String,
    startRun: () -> Unit,
    pauseRun: () -> Unit,
    finishRun: () -> Unit,
    cancelRun: ()->Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = timer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 35.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp
        )
        if (isTracking) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        pauseRun.invoke()
                    }, modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(Blue800)
                ) {
                    Text(
                        text = "Pause",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp),
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        finishRun.invoke()
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(Blue800)
                ) {
                    Text(
                        text = "Finish",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp),
                        color = Color.White
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        startRun.invoke()
                    },
                    modifier = Modifier
                        .wrapContentSize().padding(end = 10.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(Blue800)
                ) {
                    Text(
                        text = "Start",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp),
                        color = Color.White
                    )
                }
                if (!isFirstRun)
                    Button(
                        onClick = {
                           cancelRun.invoke()
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 10.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(Blue800)
                    ) {
                        Text(
                            text = "Cancel",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                            color = Color.White
                        )
                    }
            }
        }
    }
}