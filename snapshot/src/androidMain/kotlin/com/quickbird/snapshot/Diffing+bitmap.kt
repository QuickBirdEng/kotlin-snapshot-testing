package com.quickbird.snapshot

import android.graphics.Bitmap
import android.util.Log
import android.graphics.Color as AndroidColor

private var maximumDeltaE: Double? = null

/**
 * A Bitmap comparison diffing strategy for comparing images based on pixel equality.
 *
 * @param colorDiffing A function that compares two colors and returns a color representing the difference.
 * @param tolerance Total percentage of pixels that must match between image. The default value of 0.0% means all pixels must match
 * @param perceptualTolerance Percentage each pixel can be different from source pixel and still considered
 * a match. The default value of 0.0% means pixels must match perfectly whereas the recommended value of 0.02% mimics the
 * [precision](http://zschuessler.github.io/DeltaE/learn/#toc-defining-delta-e) of the human eye.
 */
fun Diffing.Companion.bitmap(
    colorDiffing: Diffing<Color>,
    tolerance: Double = 0.0,
    perceptualTolerance: Double = 0.0
) = Diffing<Bitmap> { first, second ->
    val difference = first.differenceTo(second, perceptualTolerance)

    if (difference <= tolerance) {
        Log.d("SnapshotDiffing", "Actual image difference ${difference.toBigDecimal().toPlainString()}, required image difference ${tolerance.toBigDecimal().toPlainString()}")
        null
    } else {
        var log = "Actual image difference ${difference.toBigDecimal().toPlainString()} is greater than max allowed ${tolerance.toBigDecimal().toPlainString()}"
        maximumDeltaE?.let { log += ", Actual perceptual difference ${it.toBigDecimal().toPlainString()} is greater than max allowed ${perceptualTolerance.toBigDecimal().toPlainString()}" }
        Log.e("SnapshotDiffing", log)

        first.config.let {
            first.copy(it, true).apply {
                updatePixels { x, y, color ->
                    if (x < second.width && y < second.height)
                        colorDiffing(color, second.getPixel(x, y).color) ?: color
                    else color
                }
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
        // Find the maximum delta E value for logging purposes
        //
        maximumDeltaE = deltaEPixels.maxOrNull() ?: 0.0
        deltaEPixels.count { it > (perceptualTolerance) }
    } else {
        thisPixels
            .zip(otherPixels, Color::equals)
            .count { !it }
    }

    return pixelDifferenceCount.toDouble() / thisPixels.size
}