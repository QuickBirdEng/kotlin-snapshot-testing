package com.quickbird.snapshot

data class FileSnapshotting<Value, Format>(
    val snapshotting: Snapshotting<Value, Format>,
    val fileStoring: FileStoring<Format>
) {
    companion object
}

fun <Value, Format> Snapshotting<Value, Format>.fileSnapshotting(
    fileStoring: FileStoring<Format>
) = FileSnapshotting(this, fileStoring)
