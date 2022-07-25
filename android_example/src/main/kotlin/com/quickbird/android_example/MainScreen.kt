package com.quickbird.android_example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.quickbird.android_example.R

@Preview
@Composable
fun MainScreen(onSettingsButtonClick: () -> Unit = {}) {
    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val (image, settingsButton) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.quickbird),
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .constrainAs(image) {
                        val margin = 32.dp

                        top.linkTo(parent.top, margin = margin)
                        bottom.linkTo(parent.bottom, margin = margin)
                        start.linkTo(parent.start, margin = margin)
                        end.linkTo(parent.end, margin = margin)
                    },
                contentScale = ContentScale.Fit,
                contentDescription = "QuickBird"
            )

            FloatingActionButton(
                shape = RoundedCornerShape(25),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                onClick = onSettingsButtonClick,
                elevation = FloatingActionButtonDefaults.zero(),
                modifier = Modifier
                    .constrainAs(settingsButton) {
                        val margin = 16.dp
                        bottom.linkTo(parent.bottom, margin = margin)
                        end.linkTo(parent.end, margin = margin)
                    }
                    .testTag("Settings")
            ) {
                Icon(Icons.Filled.Settings, "Settings")
            }
        }
    }
}

@Composable
private fun FloatingActionButtonDefaults.zero() = elevation(0.dp, 0.dp, 0.dp, 0.dp)
