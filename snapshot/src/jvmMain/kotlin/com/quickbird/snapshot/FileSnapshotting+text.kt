package com.quickbird.snapshot

val FileSnapshotting.Companion.text
    get() = Snapshotting.lines.fileSnapshotting(FileStoring.text)
