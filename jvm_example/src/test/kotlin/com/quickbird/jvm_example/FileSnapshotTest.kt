package com.quickbird.jvm_example

import com.quickbird.snapshot.*
import com.quickbird.snapshot.util.identity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FileSnapshotTest : JUnitFileSnapshotTest() {

    private val quickBird = QuickBird(numberOfEatenWorms = 42)

    @Test
    fun quickBird() = runTest {
        Snapshotting
            .quickBird
            .fileSnapshotting(FileStoring.text)
            .snapshot(quickBird)
    }
}
