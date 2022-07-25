package com.quickbird.snapshot

abstract class AndroidFileSnapshotTest(record: Boolean? = null) : JUnitFileSnapshotTest(record) {

    suspend fun <Value, Format> FileSnapshotting<Value, Format>.snapshotToFilesDir(
        value: Value,
        record: Boolean = false,
        fileSnapshottingNames: FileSnapshottingNames = FileSnapshottingNames.filesDir
    ) = snapshot(
        value = value,
        record = record,
        fileSnapshottingNames = fileSnapshottingNames
    )
}
