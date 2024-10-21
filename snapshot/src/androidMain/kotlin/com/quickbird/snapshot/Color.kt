package com.quickbird.snapshot

import android.util.Log
import androidx.annotation.ColorInt
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class Color(@ColorInt val value: Int)

val @receiver:ColorInt Int.color
    get() = Color(this)

fun Color.deltaE(other: Color): Double {
    if (this == other) {
        return 0.0
    }
    // Compute the Delta E 2000 difference between the two colors in the CIELAB color space and return whether it's within the perceptual tolerance
    //
    return min(this.difference(other) / 100, 1.0)
}

// Convert the color to the Lch color space
//
private fun Color.toLch(): DoubleArray {
    val lab = this.toLAB()
    val l = lab[0]
    val a = lab[1]
    val b = lab[2]

    val c = sqrt(a * a + b * b)
    val h = atan2(b, a) * (180 / PI)
    return doubleArrayOf(l, c, if (h >= 0) h else h + 360)
}

// Convert the color to the CIELAB color space
//
private fun Color.toLAB(): DoubleArray {
    val r = (value shr 16 and 0xff) / 255.0
    val g = (value shr 8 and 0xff) / 255.0
    val b = (value and 0xff) / 255.0

    val x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375
    val y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750
    val z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041

    val l1 = 116 * f(y / 1.0) - 16
    val a1 = 500 * (f(x / 0.95047) - f(y / 1.0))
    val b1 = 200 * (f(y / 1.0) - f(z / 1.08883))

    return doubleArrayOf(l1, a1, b1)
}

private fun f(t: Double): Double {
    return if (t > 0.008856) cbrt(t) else (7.787 * t) + (16 / 116.0)
}

// Calculates CIEDE2000 (Delta E 2000) between two colors in the CIELAB color space returning a value between 0-100 (0 means no difference, 100 means completely opposite)
//
// This is the most recent and accurate formula, which includes corrections for perceptual uniformity
// Platform difference: iOS uses CIE94 (Delta E 1994) for color difference calculations
//
private fun Color.difference(other: Color): Double {
    val (L1, C1, H1) = this.toLch()
    val (L2, C2, H2) = other.toLch()

    val deltaL = L2 - L1
    val meanL = (L1 + L2) / 2

    val deltaC = C2 - C1
    val meanC = (C1 + C2) / 2

    val deltaH = H2 - H1
    val meanH = (H1 + H2) / 2

    val T = 1 - 0.17 * kotlin.math.cos(Math.toRadians(meanH - 30)) +
            0.24 * kotlin.math.cos(Math.toRadians(2 * meanH)) +
            0.32 * kotlin.math.cos(Math.toRadians(3 * meanH + 6)) -
            0.20 * kotlin.math.cos(Math.toRadians(4 * meanH - 63))

    val deltaTheta = 30 * kotlin.math.exp(-((meanH - 275) / 25).pow(2.0))
    val Rc = 2 * sqrt((meanC.pow(7.0)) / (meanC.pow(7.0) + 25.0.pow(7.0)))
    val Sl = 1 + (0.015 * (meanL - 50).pow(2.0)) / sqrt(20 + (meanL - 50).pow(2.0))
    val Sc = 1 + 0.045 * meanC
    val Sh = 1 + 0.015 * meanC * T
    val Rt = -kotlin.math.sin(Math.toRadians(2 * deltaTheta)) * Rc

    val deltaE = sqrt(
        (deltaL / Sl).pow(2.0) +
        (deltaC / Sc).pow(2.0) +
        (deltaH / Sh).pow(2.0) +
        Rt * (deltaC / Sc) * (deltaH / Sh)
    )
    Log.d("SnapshotDiffing", "Delta E: $deltaE")
    return deltaE
}