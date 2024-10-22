package com.quickbird.snapshot

import android.graphics.Color as AndroidColor
import androidx.annotation.ColorInt
import kotlin.collections.component1
import kotlin.math.abs
import kotlin.math.cbrt
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class Color(@ColorInt val value: Int)

val @receiver:ColorInt Int.color
    get() = Color(this)

/**
 * Calculates [Delta E (1994)](http://zschuessler.github.io/DeltaE/learn/#toc-delta-e-94) between
 * two colors in the CIE LAB color space returning a value between 0.0 - 1.0 (0.0 means no difference, 1.0 means completely opposite)
 */
fun Color.deltaE(other: Color): Double {
    if (this == other) {
        return 0.0
    }
    // Delta E (1994) is in a 0-100 scale, so we need to divide by 100 to transform it to a percentage
    //
    return min(this.deltaE1994(other) / 100, 1.0)
}

/**
 * Convert the color to the CIE XYZ color space within nominal range of [0.0, 1.0]
 * using sRGB color space and D65 white reference white
 */
private fun Color.toXYZ(): DoubleArray {
    // Values must be within nominal range of [0.0, 1.0]
    //
    var r = AndroidColor.red(this.value) / 255.0
    var g = AndroidColor.green(this.value) / 255.0
    var b = AndroidColor.blue(this.value) / 255.0

    // Inverse sRGB companding
    //
    r = if (r <= 0.04045) {
        r / 12.92
    } else {
        ((r + 0.055) / 1.055).pow(2.4)
    }

    g = if (g > 0.04045) {
        ((g + 0.055) / 1.055).pow(2.4)
    } else {
        g / 12.92
    }

    b = if (b > 0.04045) {
        ((b + 0.055) / 1.055).pow(2.4)
    } else {
        b / 12.92
    }

    // Linear RGB to XYZ using sRGB color space and D65 white reference white
    //
    return doubleArrayOf(
        (0.4124564 * r + 0.3575761 * g + 0.1804375 * b),
        (0.2126729 * r + 0.7151522 * g + 0.0721750 * b),
        (0.0193339 * r + 0.1191920 * g + 0.9503041 * b)
    )
}

/**
 * Convert the color to the CIE LAB color space using sRGB color space and D65 white reference white
 */
private fun Color.toLAB(): DoubleArray {
    val (x, y, z) = this.toXYZ()

    // CIE standard illuminant D65
    //
    val Xr = 0.9504
    val Yr = 1.000
    val Zr = 1.0888

    var xr = x / Xr
    var yr = y / Yr
    var zr = z / Zr

    val e = 0.008856
    val k = 903.3

    val fx = if ( xr > e ) {
        cbrt(xr)
    } else {
        ((k * xr) + 16) / 116.0
    }

    val fy = if ( yr > e ) {
        cbrt(yr)
    } else {
        ((k * yr) + 16) / 116.0
    }

    val fz = if ( zr > e ) {
        cbrt(zr)
    } else {
        ((k * zr) + 16) / 116.0
    }

    return doubleArrayOf(
        116 * fy - 16,
        500 * (fx - fy),
        200 * (fy - fz)
    )
}

/**
 * Calculates [Delta E (1994)](http://zschuessler.github.io/DeltaE/learn/#toc-delta-e-94) between
 * two colors in the CIE LAB color space returning a value between 0-100 (0 means no difference, 100 means completely opposite)
 */
private fun Color.deltaE1994(other: Color): Double {
    val (l1, a1, b1) = this.toLAB()
    val (l2, a2, b2) = other.toLAB()

    val deltaL = l1 - l2

    val c1 = sqrt(a1.pow(2) + b1.pow(2))
    val c2 = sqrt(a2.pow(2) + b2.pow(2))
    val deltaC = c1 - c2

    val deltaA = a1 - a2
    val deltaB = b1 - b2
    val deltaH = sqrt(abs(deltaA.pow(2) + deltaB.pow(2) - deltaC.pow(2)))

    val sl = 1
    val kl = 1
    val kc = 1
    val kh = 1
    val k1 = 0.045
    val k2 = 0.015

    val sc = 1 + k1 * c1
    val sh = 1 + k2 * c1

    return sqrt(
        (deltaL / (kl * sl)).pow(2) +
                (deltaC / (kc * sc)).pow(2) +
                (deltaH / (kh * sh)).pow(2)
    )
}