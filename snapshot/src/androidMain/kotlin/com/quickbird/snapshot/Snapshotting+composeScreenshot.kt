package com.quickbird.snapshot

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot

val Snapshotting.Companion.composeNodeScreenshot
    get() = Snapshotting(
        diffing = Diffing.bitmap(colorDiffing = Diffing.colorMean),
        snapshot = { node: SemanticsNodeInteraction -> node.captureToImage().asAndroidBitmap() }
    )

val Snapshotting.Companion.composeScreenshot
    get() = composeNodeScreenshot.pullback { rule: ComposeTestRule -> rule.onRoot() }

/**
 * This [Snapshotting] can be used together with Robolectric.
 * The above ones currently cannot.
 * ([issue](https://github.com/robolectric/robolectric/issues/8071))
  */
val Snapshotting.Companion.composeActivityScreenshot
    get() = activityScreenshot.pullback { rule: AndroidComposeTestRule<*, *> ->
        rule.awaitIdle()
        rule.activity
    }
