package com.quickbird.snapshot.androidTransfer

import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTestFiles.Type.Actual
import com.quickbird.snapshot.androidTransfer.AndroidSnapshotTestFiles.Type.Expected
import com.quickbird.snapshot.util.listContainingFiles
import java.io.File
import kotlin.test.assertEquals

@Suppress("SdCardPath") // We don't have android dependencies here ¯\_(ツ)_/¯
data class AndroidSnapshotTestFiles(
    private val directory: AndroidSnapshotDirectory,
    private val appBundle: String,
    private val diffFilePrefix: String = "diff",
    private val androidDirectory: String = "/data/data/$appBundle/files/snapshots",
    private val tmpAndroidDirectory: String = "/sdcard/snapshot_tmp",
    private val deviceIds: List<String> = listAdbDevices()
) {
    enum class Type(internal val directorySuffix: String) {
        Expected(""),
        Actual("actual")
    }

    val diffs
        get() = diffFiles(Expected) +
                diffFiles(Actual) +
                (testFiles(Expected) differenceTo testFiles(Actual)) +
                (testFiles(Actual) differenceTo testFiles(Expected))

    fun pull(type: Type) = deviceIds.map { deviceId ->

        val directory = directory(suffix = type.directorySuffix, deviceId = deviceId)

        directory.deleteRecursively()
        directory.mkdirs()

        val path = directory.absolutePath

        val directPullResult = runAdb("pull", androidDirectory, path, deviceId = deviceId)

        // If directly pulling doesn't work, try through a tmp directory
        if (directPullResult != 0) {
            runAdbShell("cp", "-R", androidDirectory, tmpAndroidDirectory, appBundle = appBundle)
                .apply { assertEquals(0, this, "Copy to tmp directory failed!") }

            runAdb("pull", tmpAndroidDirectory, path, deviceId = deviceId)
                .apply { assertEquals(0, this, "Adb pull failed!") }

            runAdbShell("rm", "-R", tmpAndroidDirectory, appBundle = appBundle)
                .apply { assertEquals(0, this, "Deleting adb tmp directory failed!") }
        }

        File(path)
    }

    fun delete(type: Type) = deviceIds.map { deviceId ->
        directory(suffix = type.directorySuffix, deviceId = deviceId).deleteRecursively()
    }

    private fun diffFiles(type: Type) = testFiles(type)
        .filter { it.name.split(".").first().endsWith(diffFilePrefix) }.toSet()

    private fun testFiles(type: Type) = deviceIds.flatMap { deviceId ->
        directory(suffix = type.directorySuffix, deviceId = deviceId)
            .listContainingFiles()
            .flatMap { it.listContainingFiles() }
    }

    private val File.relativeFilePath
        get() = parentFile.name + File.separator + name

    private infix fun List<File>.differenceTo(other: List<File>) = this
        .filterNot { file ->
            other.map { otherFile -> otherFile.relativeFilePath }.contains(file.relativeFilePath)
        }
        .toSet()
}
