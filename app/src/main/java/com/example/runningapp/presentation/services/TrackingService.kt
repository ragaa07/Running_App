package com.example.runningapp.presentation.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.runningapp.R
import com.example.runningapp.presentation.util.Constants.ACTION_PAUSE_SERVICE
import com.example.runningapp.presentation.util.Constants.ACTION_START_RESUME_SERVICE
import com.example.runningapp.presentation.util.Constants.ACTION_STOP_SERVICE
import com.example.runningapp.presentation.util.Constants.FAST_LOCATION_INTERVAL
import com.example.runningapp.presentation.util.Constants.LOCATION_UPDATE_INTERVAL
import com.example.runningapp.presentation.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.runningapp.presentation.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.runningapp.presentation.util.Constants.NOTIFICATION_ID
import com.example.runningapp.presentation.util.Constants.TIMER_UPDATE_INTERVAL
import com.example.runningapp.presentation.util.formatTimeToStopWatchFormat
import com.example.runningapp.presentation.util.hasLocationPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias polyLine = MutableList<LatLng>
typealias polyLines = MutableList<polyLine>

@AndroidEntryPoint
class TrackingService : LifecycleService() {
    var serviceIsKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder
    private val runningTimesInSeconds = MutableLiveData<Long>()
    var isTimerEnabled = false
    var labTime = 0L
    var startTime = 0L
    var runningTime = 0L
    var lastSecondTimestamp = 0L

    companion object {
        var isFirstRun = mutableStateOf(true)
        val runningTimeInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<polyLines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        runningTimeInMillis.postValue(0L)
        runningTimesInSeconds.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        curNotificationBuilder = notificationBuilder
        isTracking.observe(this, {
            updateLocationRequest(it)
            updateNotification(it)
        })
    }

    private fun killTheService() {
        serviceIsKilled = true
        isFirstRun.value = true
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            when (it.action) {
                ACTION_START_RESUME_SERVICE -> {
                    if (isFirstRun.value) {
                        startForegroundService()
                        isFirstRun.value = false
                    } else {
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseTheService()
                }
                ACTION_STOP_SERVICE -> {
                    killTheService()
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startTimer() {
        addEmptyPolyLine()
        isTracking.postValue(true)
        startTime = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                labTime = System.currentTimeMillis() - startTime
                runningTimeInMillis.postValue(runningTime + labTime)
                if (runningTimeInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    runningTimesInSeconds.postValue(lastSecondTimestamp + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            runningTime += labTime
        }
    }

    private fun updateNotification(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"

        val pendingIntent = if (isTracking) {
            val pauseAction = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseAction, FLAG_UPDATE_CURRENT)
        } else {
            val resumeAction = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_RESUME_SERVICE
            }
            PendingIntent.getService(this, 1, resumeAction, FLAG_UPDATE_CURRENT)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if (!serviceIsKilled) {
            curNotificationBuilder = notificationBuilder.addAction(
                R.drawable.ic_baseline_directions_run_24,
                notificationActionText,
                pendingIntent
            )
            notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationRequest(isTracking: Boolean) {
        if (isTracking) {
            if (hasLocationPermissions()) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FAST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoints(location)
                    }
                }
            }
        }
    }

    private fun addPathPoints(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }

    }

    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun pauseTheService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        createNotificationChannel(notificationManager)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        runningTimesInSeconds.observe(this, {
            if (!serviceIsKilled) {
                val notification =
                    curNotificationBuilder.setContentText(formatTimeToStopWatchFormat(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}