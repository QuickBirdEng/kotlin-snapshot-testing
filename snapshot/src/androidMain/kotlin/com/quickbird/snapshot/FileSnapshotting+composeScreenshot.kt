package com.quickbird.snapshot

val FileSnapshotting.Companion.composeScreenshot
    get() = Snapshotting.composeScreenshot.fileSnapshotting

val FileSnapshotting.Companion.composeNodeScreenshot
    get() = Snapshotting.composeNodeScreenshot.fileSnapshotting
