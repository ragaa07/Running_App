package com.example.runningapp.presentation.trackingScreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.presentation.MainActivity
import com.example.runningapp.presentation.services.TrackingService
import com.example.runningapp.presentation.services.polyLine
import com.example.runningapp.presentation.ui.components.CancelRunDialog
import com.example.runningapp.presentation.ui.components.ToggleRunButtons
import com.example.runningapp.presentation.ui.theme.Blue400
import com.example.runningapp.presentation.ui.theme.RunningAppTheme
import com.example.runningapp.presentation.util.*
import com.example.runningapp.presentation.util.Constants.ACTION_START_RESUME_SERVICE
import com.example.runningapp.presentation.util.Constants.ACTION_STOP_SERVICE
import com.example.runningapp.presentation.util.Constants.CAMERA_ZOOM
import com.example.runningapp.presentation.util.Constants.POLYLINE_WIDTH
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class TrackingActivity : ComponentActivity(), OnMapReadyCallback {

    private val viewModel: TrackingViewModel by viewModels<TrackingViewModel>()
    private var testMarker: Marker? = null
    private var isTracking by mutableStateOf(false)
    private var isFirstRun by mutableStateOf(TrackingService.isFirstRun)
    private var pathPoints = mutableListOf<polyLine>()
    private var currentTimeInMillis = 0L
    private val weight = 80
    private var formattedTime by mutableStateOf("00 : 00 : 00 : 00")
    private lateinit var mapView: MapView
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColorToBlue()
        subscribeToObservers()
        val mapViewBundle = savedInstanceState?.get(MAPVIEW_BUNDLE_KEY) as? Bundle
        mapView = MapView(this)
        mapView.onCreate(mapViewBundle)
        setContent {
            val (dialogState, setDialogState) = remember { mutableStateOf(false) }
            RunningAppTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                    ) {
                        CancelRunDialog(
                            cancelRun = {
                                stopRun()
                            },
                            openDialog = dialogState,
                            setDialogState = setDialogState
                        )
                        BuildMap(onMapReadyCallback = this@TrackingActivity)
                        TopAppBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            backgroundColor = Color.Transparent,
                            elevation = 0.dp,
                            navigationIcon = {
                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    IconButton(onClick = { onBackPressed() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            actions = {
                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    shape = RoundedCornerShape(15.dp)
                                ) {
                                    IconButton(onClick = { }) {
                                        Icon(
                                            imageVector = Icons.Default.Save,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            title = {}
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        elevation = 5.dp,
                        backgroundColor = Blue400
                    ) {

                        ToggleRunButtons(
                            isTracking = isTracking,
                            isFirstRun = isFirstRun.value,
                            timer = formattedTime,
                            startRun = { sendCommandToService(ACTION_START_RESUME_SERVICE) },
                            pauseRun = { sendCommandToService(Constants.ACTION_PAUSE_SERVICE) },
                            finishRun = {
                                zoomToSeeTheWholeRun()
                                endAndSaveRun()
                            },
                            cancelRun = { setDialogState(true) }
                        )
                    }
                }
            }
        }
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(this, {
            isTracking = it
        })

        TrackingService.pathPoints.observe(this, {
            pathPoints = it
            addLatestPolyLine()
            moveCameraToUser()
        })
        TrackingService.runningTimeInMillis.observe(this, {
            currentTimeInMillis = it
            formattedTime = formatTimeToStopWatchFormat(currentTimeInMillis, true)
        })
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    CAMERA_ZOOM
                )
            )
        }
    }

    private fun zoomToSeeTheWholeRun() {
        val bounds = LatLngBounds.builder()
        for (polyLine in pathPoints) {
            for (pos in polyLine) {
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endAndSaveRun() {
        map?.snapshot {
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += calculateDistance(polyLine = polyline).toInt()
            }
            val aveSpeed = (distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60)
            val dateTime = Date().time
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = RunsDomainModel(
                img = it,
                timestamp = dateTime,
                avgSpeedInKMH = aveSpeed,
                burnedCalories = caloriesBurned,
                distanceInMeters = distanceInMeters,
                timeInMillis = currentTimeInMillis
            )
            viewModel.addRun(run)
            Toast.makeText(this, "Your Run Saved Successfully", Toast.LENGTH_SHORT).show()
            stopRun()
        }
    }

    private fun addAllPolyLines() {
        for (polyLine in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.Red.toArgb())
                .width(POLYLINE_WIDTH)
                .addAll(polyLine)
            map?.addPolyline(polylineOptions)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        testMarker?.remove()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        startActivity(
            Intent(
                this@TrackingActivity,
                MainActivity::class.java
            )
        )
    }

    private fun addLatestPolyLine() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.Red.toArgb())
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: String) {
        Intent(this, TrackingService::class.java).also {
            it.action = action
            this.startForegroundService(it)

        }
    }

    @Composable
    private fun BuildMap(onMapReadyCallback: OnMapReadyCallback) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(), // Occupy the max size in the Compose UI tree
            factory = {
                mapView
            },
            update = {
                mapView.getMapAsync(onMapReadyCallback)
                mapView.getMapAsync {
                    map = it
                    addAllPolyLines()
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        formattedTime = "00 : 00 : 00 : 00"
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

}