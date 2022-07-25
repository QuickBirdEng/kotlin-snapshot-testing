package com.quickbird.snapshot

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import java.io.File

val FileSnapshottingNames.Companion.filesDir
    get() = default.copy(
        parentDirectory = getInstrumentation().targetContext.filesDir.canonicalPath +
                File.separator +
                default.parentDirectory
    )
