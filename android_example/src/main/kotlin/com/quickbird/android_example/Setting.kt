package com.quickbird.android_example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Setting(title: String = "", Content: @Composable () -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (title != "") Text(text = title, fontWeight = FontWeight.Light)
        Content()
    }
}

@Preview
@Composable
fun SwitchSetting(
    title: String = "",
    default: Boolean = true
) = Setting(title) {
    var checked by remember { mutableStateOf(default) }
    Switch(checked = checked, onCheckedChange = { checked = it })
}

@Preview
@Composable
fun NumberSetting(
    title: String = "",
    default: Int = 42
) = Setting(title = title) {
    var value by remember { mutableStateOf(default) }

    OutlinedTextField(
        value = value.toString(),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onValueChange = { value = it.toIntOrNull() ?: default }
    )
}
