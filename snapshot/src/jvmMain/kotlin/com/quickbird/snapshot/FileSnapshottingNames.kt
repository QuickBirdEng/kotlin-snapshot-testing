package com.quickbird.snapshot

data class FileSnapshottingNames(
    val referenceFilePrefix: String,
    val diffFilePrefix: String,
    val parentDirectory: String
) {
    companion object {
        val default = FileSnapshottingNames(
            referenceFilePrefix = "reference",
            diffFilePrefix = "diff",
            parentDirectory = "snapshot"
        )
    }
}
