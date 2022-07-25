package com.quickbird.snapshot

import java.io.File

data class FileStoring<Value>(
    val store: (Value, File) -> Unit,
    val load: (File) -> Value,
    val fileExtension: String = ""
) {
    companion object
}
