package com.quickbird.snapshot

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

internal fun Bitmap.asByteArray(): ByteArray? = ByteArrayOutputStream().use { outputStream ->
    compress(Bitmap.CompressFormat.PNG, 0, outputStream)
    outputStream.toByteArray()
}
