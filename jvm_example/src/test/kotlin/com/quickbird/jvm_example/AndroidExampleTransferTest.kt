package com.quickbird.jvm_example

import com.quickbird.snapshot.androidTransfer.AndroidSnapshotDirectory
import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTestFiles
import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTransferTest
import java.io.File

class AndroidExampleTransferTest : AndroidSnapshotTransferTest(
    androidSnapshotTestFiles = AndroidSnapshotTestFiles(
        appBundle = "com.quickbird.android_example",
        directory = AndroidSnapshotDirectory(parent = File("snapshot"))
    )
)
