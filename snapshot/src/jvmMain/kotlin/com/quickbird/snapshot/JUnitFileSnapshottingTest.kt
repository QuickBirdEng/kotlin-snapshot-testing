package com.quickbird.snapshot

import org.junit.Rule
import org.junit.rules.TestName

abstract class JUnitFileSnapshotTest(private val record: Boolean? = null) {
    @get:Rule
    val testName: TestName = TestName()

    private val directoryName
        get() = this::class.simpleName ?: "FileSnapshotTest"

    suspend fun <Value, Format> FileSnapshotting<Value, Format>.snapshot(
        value: Value,
        record: Boolean = false,
        fileSnapshottingNames: FileSnapshottingNames = FileSnapshottingNames.default
    ) = with(fileSnapshottingNames) {
        snapshot(
            value = value,
            record = this@JUnitFileSnapshotTest.record ?: record,
            fileName = testName.methodName + "_$referenceFilePrefix",
            diffFileName = testName.methodName + "_$diffFilePrefix",
            directoryName = directoryName,
            path = parentDirectory
        )
    }
}
