package com.example.runningapp.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.runningapp.presentation.ui.theme.Blue400

@Composable
fun CancelRunDialog(
    cancelRun: () -> Unit,
    openDialog: Boolean,
    setDialogState: (Boolean) -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { setDialogState(false) },
            title = {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Cancel")
                    Text(
                        text = "Cancel Run ",
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    text = "Are you sure to cancel the run ?",
                    modifier = Modifier.padding(15.dp),
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                Button(onClick = {
                    setDialogState(false)
                    cancelRun.invoke()
                }, colors = ButtonDefaults.buttonColors(Blue400)) {
                    Text(text = "Yes",color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { setDialogState(false) },
                    colors = ButtonDefaults.buttonColors(Blue400)
                ) {
                    Text(text = "No",color = Color.White)
                }
            })
    }
}