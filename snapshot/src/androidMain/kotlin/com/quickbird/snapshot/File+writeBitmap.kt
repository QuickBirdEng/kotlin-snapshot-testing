package com.quickbird.snapshot

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File

internal fun File.writeAsPng(bitmap: Bitmap) {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
    val byteArray = outputStream.toByteArray()
    writeBytes(byteArray)
}
