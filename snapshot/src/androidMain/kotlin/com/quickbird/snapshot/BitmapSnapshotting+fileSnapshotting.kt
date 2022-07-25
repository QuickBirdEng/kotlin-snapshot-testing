package com.quickbird.snapshot

import android.graphics.Bitmap

val <Value> Snapshotting<Value, Bitmap>.fileSnapshotting
    get() = fileSnapshotting(fileStoring = FileStoring.bitmap)
