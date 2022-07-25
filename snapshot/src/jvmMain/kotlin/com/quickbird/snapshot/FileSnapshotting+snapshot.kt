package com.quickbird.snapshot

import java.io.File
import kotlin.test.assertTrue

suspend fun <Value, Format> FileSnapshotting<Value, Format>.snapshot(
    value: Value,
    record: Boolean = false,
    directoryName: String,
    fileName: String = "reference",
    diffFileName: String = "diff",
    path: String = "snapshot"
) {

    fun file(name: String): File {
        val extension = fileStoring.fileExtension
        val filename = if (extension.isBlank()) name else "$name.$extension"
        val snapshotDirectory = File(path, directoryName)

        if (!snapshotDirectory.exists()) assertTrue(
            snapshotDirectory.mkdirs(),
            "Not able to create snapshot directory: ${snapshotDirectory.absolutePath}"
        )

        return File(snapshotDirectory, filename)
    }

    val snapshot = snapshotting.snapshot(value)
    val referenceFile = file(fileName)
    val diffFile = file(diffFileName)
    val fileStoring = fileStoring.asserted

    if (record) {
        fileStoring.store(snapshot, referenceFile)
        diffFile.deleteRecursively()
        throw AssertionError("Stored snapshot to: ${referenceFile.absolutePath}")
    } else {
        val reference = fileStoring.load(referenceFile)
        val diff = snapshotting.diffing(reference, snapshot)
        if (diff == null) {
            diffFile.deleteRecursively()
            return
        }

        fileStoring.store(diff, diffFile)

        throw AssertionError(
            "Snapshot is different from the reference!\nDiff stored to: ${diffFile.absolutePath}"
        )
    }
}
