package com.quickbird.snapshot

import android.graphics.Bitmap

internal fun Bitmap.updatePixels(update: (x: Int, y: Int, color: Color) -> Color) {
    (0 until width).map { x ->
        (0 until height).map { y ->
            val updated = update(x, y, getPixel(x, y).color)
            setPixel(x, y, updated.value)
        }
    }
}

internal val Bitmap.pixels
    get() = (0 until width).flatMap { x ->
        (0 until height).map { y ->
            getPixel(x, y).color
        }
    }
