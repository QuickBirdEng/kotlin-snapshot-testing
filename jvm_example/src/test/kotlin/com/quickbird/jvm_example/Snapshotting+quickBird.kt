package com.quickbird.jvm_example

import com.quickbird.snapshot.Snapshotting
import com.quickbird.snapshot.lines
import com.quickbird.snapshot.pullback

val Snapshotting.Companion.quickBird
    get() = lines.pullback(QuickBird::toString)
