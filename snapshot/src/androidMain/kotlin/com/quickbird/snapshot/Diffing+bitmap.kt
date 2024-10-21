package com.quickbird.snapshot

import android.graphics.Bitmap
import android.util.Log
import android.graphics.Color as AndroidColor

private var maximumDeltaE: Double? = null

fun Diffing.Companion.bitmap(
    colorDiffing: Diffing<Color>,
    tolerance: Double = 0.0,
    perceptualTolerance: Double = 0.0
) = Diffing<Bitmap> { first, second ->
    val difference = first.differenceTo(second, perceptualTolerance)

    if (difference <= tolerance) {
        null
    } else {
        var log = "Actual image precision $difference is greater than required $tolerance"
        if (maximumDeltaE != null) log += ", Actual perceptual precision $maximumDeltaE is greater than required $perceptualTolerance"
        Log.e("SnapshotDiffing", log)

        first.copy(first.config, true).apply {
            updatePixels { x, y, color ->
                if (x < second.width && y < second.height)
                    colorDiffing(color, second.getPixel(x, y).color) ?: color
                else color
            }
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

    val deltaEPixels = thisPixels
        .zip(otherPixels, Color::deltaE)
    val deltaEDifference = deltaEPixels.count { it < perceptualTolerance }
    maximumDeltaE = deltaEPixels.maxOrNull() ?: 0.0

    return deltaEDifference.toDouble() / thisPixels.size
}