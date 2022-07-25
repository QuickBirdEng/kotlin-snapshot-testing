package com.quickbird.snapshot

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot

val Snapshotting.Companion.composeNodeScreenshot
    get() = Snapshotting(
        diffing = Diffing.bitmap(colorDiffing = Diffing.intMean),
        snapshot = { node: SemanticsNodeInteraction -> node.captureToImage().asAndroidBitmap() }
    )

val Snapshotting.Companion.composeScreenshot
    get() = composeNodeScreenshot.pullback { rule: AndroidComposeTestRule<*, *> -> rule.onRoot() }
