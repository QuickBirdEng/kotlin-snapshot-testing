package com.quickbird.snapshot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.printToString

val Snapshotting.Companion.composeNodeStringTree
    get() = Snapshotting(
        diffing = Diffing.lines,
        snapshot = { node: SemanticsNodeInteraction -> node.printToString() }
    )

val Snapshotting.Companion.composeStringTree
    get() = composeNodeStringTree.pullBackToTestRule()
