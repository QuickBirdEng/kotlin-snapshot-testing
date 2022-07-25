package com.quickbird.android_example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues).padding(16.dp)) {
            SwitchSetting(title = "Hungry?")
            SwitchSetting(title = "Tired?", default = false)
            NumberSetting(title = "Eaten Worms:")
        }
    }
}
