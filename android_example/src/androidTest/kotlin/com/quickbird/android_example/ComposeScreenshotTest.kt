package com.quickbird.android_example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.quickbird.snapshot.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class ComposeScreenshotTest : AndroidFileSnapshotTest() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreenScreenshot() = runTest {
        FileSnapshotting
            .composeScreenshot
            .snapshotToFilesDir(composeTestRule)
    }

    @Test
    fun settingsButtonScreenshot() = runTest {
        FileSnapshotting
            .composeNodeScreenshot
            .snapshotToFilesDir(composeTestRule.onNodeWithTag("Settings"))
    }

    @Test
    fun settingsScreenScreenshot() = runTest {
        composeTestRule.onNodeWithTag("Settings").performClick()

        FileSnapshotting
            .composeScreenshot
            .snapshotToFilesDir(composeTestRule)
    }

    @Test
    fun settingsScreenComposeTree() = runTest {
        composeTestRule.onNodeWithTag("Settings").performClick()

        FileSnapshotting
            .composeStringTree
            .snapshotToFilesDir(composeTestRule)
    }

    @Test
    fun mainScreenActivityScreenshot() = runTest {
        Snapshotting
            .composeActivityScreenshot
            .fileSnapshotting
            .snapshotToFilesDir(composeTestRule)
    }

    @Test
    fun settingsScreenActivityScreenshot() = runTest {
        composeTestRule.onNodeWithTag("Settings").performClick()

        Snapshotting
            .composeActivityScreenshot
            .fileSnapshotting
            .snapshotToFilesDir(composeTestRule)
    }
}
