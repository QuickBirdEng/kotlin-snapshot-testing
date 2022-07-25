package com.quickbird.snapshot.androidTransfer

import com.quickbird.snapshot.JUnitFileSnapshotTest
import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTestFiles.Type.Actual
import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTestFiles.Type.Expected
import kotlin.test.Test
import kotlin.test.assertTrue

abstract class AndroidSnapshotTransferTest(
    private val record: Boolean = false,
    private val androidSnapshotTestFiles: AndroidSnapshotTestFiles
) : JUnitFileSnapshotTest(record) {

    @Test
    fun transferFromAndroid() {
        val pulledFiles = androidSnapshotTestFiles.pull(if (record) Expected else Actual)

        if (record) throw AssertionError("Reference data transferred! $pulledFiles")

        val diffs = androidSnapshotTestFiles.diffs

        assertTrue(diffs.isEmpty(), "Diffs found!")

        androidSnapshotTestFiles.delete(Actual)
    }
}
