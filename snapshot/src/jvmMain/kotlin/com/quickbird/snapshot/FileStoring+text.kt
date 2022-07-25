package com.quickbird.snapshot

val FileStoring.Companion.text
    get() = FileStoring(
        store = { text: String, file -> file.writeText(text) },
        load = { file -> file.readText() },
        fileExtension = "txt"
    )
