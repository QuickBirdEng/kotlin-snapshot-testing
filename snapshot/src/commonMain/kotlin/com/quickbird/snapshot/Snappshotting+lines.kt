package com.quickbird.snapshot

val Snapshotting.Companion.lines
    get() = Snapshotting(diffing = Diffing.lines)
