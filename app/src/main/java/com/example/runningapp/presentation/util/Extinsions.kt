package com.example.runningapp.presentation.util

import android.Manifest
import android.app.Activity
import android.location.Location
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.example.runningapp.presentation.services.polyLine
import com.example.runningapp.presentation.ui.theme.Blue400
import com.example.runningapp.presentation.util.Constants.REQUEST_CODE_LOCATION_PERMISSION
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This extension is used for change the status bar color to transparent
 * It used inside the onCreate method in the activity that you want to change  it's status bar color
 */
fun Activity.changeStatusBarColorToBlue() {
    val window: Window = this.window
    window.statusBarColor = Blue400.toArgb()
}

fun Activity.setFullScreenWithTransparentStatusBar(
    @ColorRes color: Int = android.R.color.transparent,
    lightStatusBar: Boolean = true
) {

    this.window?.decorView?.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    this.window?.statusBarColor = ContextCompat.getColor(this, color)
}

/**
 * This extension is used format the date to ( yyyy-MM-dd hh:mm aa ) pattern
 */
fun Date.formatDateTime(): String {
    return SimpleDateFormat("MMM dd,yyyy").format(this.time)
}
fun Long.formatDateTime(): String {
    return SimpleDateFormat("MMM dd,yyyy").format(this)
}

fun Activity.hasLocationPermissions() =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

fun LifecycleService.hasLocationPermissions() =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }

fun Activity.requestPermissions(hasPermissions: Boolean) {
    if (hasPermissions) {
        return
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }
}

fun formatTimeToStopWatchFormat(
    millis: Long,
    includesMillis: Boolean = false
): String {
    var millieSecond = millis
    val hours = TimeUnit.MILLISECONDS.toHours(millieSecond)
    millieSecond -= TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millieSecond)
    millieSecond -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millieSecond)
    if (!includesMillis) {
        return "${if (hours < 10) "0" else ""}$hours : " +
                "${if (minutes < 10) "0" else ""}$minutes : " +
                "${if (seconds < 10) "0" else ""}$seconds  "
    }
    millieSecond -= TimeUnit.SECONDS.toMillis(seconds)
    millieSecond /= 10
    return "${if (hours < 10) "0" else ""}$hours : " +
            "${if (minutes < 10) "0" else ""}$minutes : " +
            "${if (seconds < 10) "0" else ""}$seconds : " +
            "${if (millieSecond < 10) "0" else ""}$millieSecond"
}

fun calculateDistance(polyLine: polyLine): Float {
    var distance = 0f
    for (i in 0..polyLine.size - 2) {
        val pos1 = polyLine[i]
        val pos2 = polyLine[i + 1]
        val result = FloatArray(1)
        Location.distanceBetween(
            pos1.latitude,
            pos1.longitude,
            pos2.latitude,
            pos2.longitude,
            result
        )
        distance += result[0]
    }
    return distance
}