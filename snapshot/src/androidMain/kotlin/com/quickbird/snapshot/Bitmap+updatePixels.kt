package com.quickbird.snapshot

import android.graphics.Bitmap

internal fun Bitmap.updatePixels(update: (x: Int, y: Int, color: Int) -> Int) {
    (0 until width).map { x ->
        (0 until height).map { y ->
            val updated = update(x, y, getPixel(x, y))
            setPixel(x, y, updated)
        }
    }
}
