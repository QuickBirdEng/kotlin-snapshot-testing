package com.quickbird.snapshot

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor

fun Diffing.Companion.bitmap(
    colorDiffing: Diffing<Color>,
    tolerance: Double = 0.0,
    perceptualTolerance: Double = 0.0
) = Diffing<Bitmap> { first, second ->
    val difference = first.differenceTo(second, perceptualTolerance)

    if (difference <= tolerance) null
    else first.copy(first.config, true).apply {
        updatePixels { x, y, color ->
            if (x < second.width && y < second.height)
                colorDiffing(color, second.getPixel(x, y).color) ?: color
            else color
        }
    }
}

val Diffing.Companion.colorRed
    get() = color(AndroidColor.RED.color)

fun Diffing.Companion.color(differenceColor: Color) = Diffing<Color> { first, second ->
    if (first == second) null
    else differenceColor
}

val Diffing.Companion.colorMean
    get() = intMean.wrap(::Color, Color::value)

val Diffing.Companion.intMean
    get() = Diffing<Int> { first, second ->
        if (first == second) null
        else first / 2 + second / 2
    }

private fun Bitmap.differenceTo(other: Bitmap, perceptualTolerance: Double): Double {
    val thisPixels = this.pixels
    val otherPixels = other.pixels
    if (thisPixels.size != otherPixels.size) return 100.0

    val differentPixelCount = thisPixels
        .zip(otherPixels) { a, b -> a.isSimilar(b, perceptualTolerance) }
        .count { !it }

    return differentPixelCount.toDouble() / thisPixels.size
}