package com.quickbird.snapshot

val <Value> Snapshotting<Value, String>.fileSnapshotting
    get() = fileSnapshotting(fileStoring = FileStoring.text)
