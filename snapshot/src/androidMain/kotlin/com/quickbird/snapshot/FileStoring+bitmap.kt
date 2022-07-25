package com.quickbird.snapshot

import android.graphics.Bitmap
import android.graphics.BitmapFactory

val FileStoring.Companion.bitmap
    get() = FileStoring<Bitmap>(
        store = { bitmap, file -> file.writeAsPng(bitmap) },
        load = { file -> BitmapFactory.decodeFile(file.absolutePath) },
        fileExtension = "png"
    )
