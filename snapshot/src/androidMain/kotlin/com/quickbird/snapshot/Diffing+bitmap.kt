package com.quickbird.snapshot

import android.graphics.Bitmap
import android.graphics.Color

// TODO: Wrap Color
fun Diffing.Companion.bitmap(colorDiffing: Diffing<Int>) = Diffing<Bitmap> { first, second ->
    if (first.asByteArray().contentEquals(second.asByteArray())) null
    else first.copy(first.config, true).apply {
        updatePixels { x, y, color ->
            if (x < second.width && y < second.height)
                colorDiffing(color, second.getPixel(x, y)) ?: color
            else color
        }
    }
}

val Diffing.Companion.colorRed
    get() = Diffing<Int> { first, second ->
        if (first == second) null
        else Color.RED
    }

val Diffing.Companion.intMean
    get() = Diffing<Int> { first, second ->
        if (first == second) null
        else first / 2 + second / 2
    }
