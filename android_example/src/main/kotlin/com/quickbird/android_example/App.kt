package com.quickbird.android_example

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Main.raw) {
        composable(Main.raw) {
            MainScreen(onSettingsButtonClick = { navController.navigate(Settings) })
            BackHandler {}
        }
        composable(Settings.raw) {
            SettingsScreen()
            BackHandler { navController.navigate(Main) }
        }
    }
}
