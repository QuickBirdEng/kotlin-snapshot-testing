package com.quickbird.snapshot

import android.graphics.Bitmap
import android.util.Log
import java.io.File
import android.graphics.Color as AndroidColor

private var maximumDeltaE: Double? = null


fun Diffing.Companion.bitmap(
    colorDiffing: Diffing<Color>,
    tolerance: Double = 0.0,  // 0.0 means exact match, 1.0 means completely different
    perceptualTolerance: Double = 0.0  // 0.0 means exact match, 1.0 means completely different
) = Diffing<Bitmap> { first, second ->
    val difference = first.differenceTo(second, perceptualTolerance)

    if (difference <= tolerance) {
        Log.d("SnapshotDiffing", "Actual image difference ${difference.toString()}, required image difference ${tolerance.toString()}")
        null
    } else {
        var log = "Actual image difference ${difference.toString()} is greater than max allowed ${tolerance.toString()}"
        if (maximumDeltaE != null) log += ", Actual perceptual difference ${maximumDeltaE.toString()} is greater than max allowed ${perceptualTolerance.toString()}"
        Log.e("SnapshotDiffing", log)

        first.copy(first.config!!, true).apply {
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
    if (thisPixels.size != otherPixels.size) return 1.0

    // Perceptually compare if the tolerance is greater than 0.0
    //
    val pixelDifferenceCount = if (perceptualTolerance > 0.0) {
        val deltaEPixels = thisPixels
            .zip(otherPixels, Color::deltaE)
        // Perceptual tolerance is given in range of 0.0 (same) - 1.0 (completely different) that
        // needs to be scaled when comparing against Delta E values between 0 (same) - 100 (completely different)
        //
        File("somefile.txt").printWriter().use { out ->
            out.println(deltaEPixels.toString())
        }
        maximumDeltaE = deltaEPixels.maxOrNull() ?: 0.0
        deltaEPixels.count { it > (perceptualTolerance) }
    } else {
        thisPixels
            .zip(otherPixels, Color::equals)
            .count { !it }
    }

    return pixelDifferenceCount.toDouble() / thisPixels.size
}