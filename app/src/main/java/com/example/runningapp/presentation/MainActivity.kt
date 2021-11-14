package com.example.runningapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.presentation.homeScreen.HomeScreen
import com.example.runningapp.presentation.homeScreen.RunsViewModel
import com.example.runningapp.presentation.settingsScreen.SettingsScreen
import com.example.runningapp.presentation.staticsScreen.StaticsScreen
import com.example.runningapp.presentation.trackingScreen.TrackingActivity
import com.example.runningapp.presentation.ui.components.BottomNavBar
import com.example.runningapp.presentation.ui.components.BottomNavItem
import com.example.runningapp.presentation.ui.theme.Blue400
import com.example.runningapp.presentation.ui.theme.RunningAppTheme
import com.example.runningapp.presentation.util.Constants.HOME_SCREEN_ROUTE
import com.example.runningapp.presentation.util.Constants.RUNNING_SCREEN_ROUTE
import com.example.runningapp.presentation.util.Constants.SETTINGS_SCREEN_ROUTE
import com.example.runningapp.presentation.util.Constants.STATICS_SCREEN_ROUTE
import com.example.runningapp.presentation.util.formatDateTime
import com.example.runningapp.presentation.util.hasLocationPermissions
import com.example.runningapp.presentation.util.requestPermissions
import com.example.runningapp.presentation.util.setFullScreenWithTransparentStatusBar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*



@AndroidEntryPoint
class MainActivity : ComponentActivity(), EasyPermissions.PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreenWithTransparentStatusBar()
        requestPermissions(hasLocationPermissions())
        setContent {
            RunningAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val backStackEntry = navController.currentBackStackEntryAsState()
                    Scaffold(bottomBar = {
                        BottomNavBar(
                            items = listOf(
                                BottomNavItem(
                                    icon = Icons.Default.Home,
                                    route = HOME_SCREEN_ROUTE,
                                    name = "Home"
                                ),
                                BottomNavItem(
                                    icon = Icons.Default.BarChart,
                                    route = STATICS_SCREEN_ROUTE,
                                    name = "Statics"
                                ),
                                BottomNavItem(
                                    icon = Icons.Default.Settings,
                                    route = SETTINGS_SCREEN_ROUTE,
                                    name = "Settings"
                                )
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    },
                        floatingActionButton = {
                            if (backStackEntry.value?.destination?.route == HOME_SCREEN_ROUTE)
                                FloatingActionButton(
                                    onClick = {
                                        val intent = Intent(this, TrackingActivity::class.java)
                                        startActivity(intent)
                                    },
                                    backgroundColor = Blue400
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsRun,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                        }) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions(hasLocationPermissions())
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HOME_SCREEN_ROUTE) {
        composable(HOME_SCREEN_ROUTE) {
            HomeScreen(Date().formatDateTime())
        }
        composable(STATICS_SCREEN_ROUTE) {
            StaticsScreen()
        }
        composable(SETTINGS_SCREEN_ROUTE) {
            SettingsScreen()
        }
        composable(RUNNING_SCREEN_ROUTE) {
            TrackingActivity()
        }
    }
}