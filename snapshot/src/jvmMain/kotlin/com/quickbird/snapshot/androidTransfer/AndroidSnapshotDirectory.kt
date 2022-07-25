package com.quickbird.snapshot.androidTransfer

import com.quickbird.snapshot.util.runIfFalse
import java.io.File

class AndroidSnapshotDirectory(
    private val parent: File,
    prefix: String = "android"
) {
    private val prefix = prefix.isEmpty().runIfFalse { "${prefix}_" } ?: ""

    @Suppress("NAME_SHADOWING")
    operator fun invoke(suffix: String, deviceId: String): File {
        val suffix = suffix.isEmpty().runIfFalse { "_$suffix" } ?: ""

        return File(parent.absolutePath + File.separator + "$prefix$deviceId$suffix")
    }
}
