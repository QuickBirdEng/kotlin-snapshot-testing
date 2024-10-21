package com.quickbird.snapshot

import android.annotation.SuppressLint
import android.graphics.Color as AndroidColor
import android.util.Log
import androidx.annotation.ColorInt
import kotlin.collections.component1
import kotlin.math.abs
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
    // Compute the Delta E 2000 difference between the two colors in the CIE Lch color space and return whether it's within the perceptual tolerance
    //
    return min(this.difference(other) / 100, 1.0)
}

// Convert the color to the CIE XYZ color space
// http://www.brucelindbloom.com/index.html?Calc.html
//
@SuppressLint("NewApi")
private fun Color.toXYZ(): DoubleArray {
    var r = AndroidColor.red(this.value) / 255.0
    var g = AndroidColor.green(this.value) / 255.0
    var b = AndroidColor.blue(this.value) / 255.0

    r = if (r > 0.04045) {
        ((r + 0.055) / 1.055).pow(2.4)
    } else {
        r / 12.92;
    }

    g = if (g > 0.04045) {
        ((g + 0.055) / 1.055).pow(2.4)
    } else {
        g / 12.92;
    }

    b = if (b > 0.04045) {
        ((b + 0.055) / 1.055).pow(2.4)
    } else {
        b / 12.92;
    }

    r *= 100
    g *= 100
    b *= 100
    Log.d("SnapshotDiffing", "R: $r, G: $g, B: $b");

    return doubleArrayOf(
        (0.4124 * r + 0.3576 * g + 0.1805 * b),
        (0.2126 * r + 0.7152 * g + 0.0722 * b),
        (0.0193 * r + 0.1192 * g + 0.9505 * b)
    ).also {
        Log.d("SnapshotDiffing", "X: ${it[0]}, Y: ${it[1]}, Z: ${it[2]}")
    }
}

// Convert the color to the CIE LAB color space
// http://www.brucelindbloom.com/index.html?Calc.html
//
@SuppressLint("NewApi")
private fun Color.toLAB(): DoubleArray {
    val (x, y, z) = this.toXYZ()

    val Xr = 95.047
    val Yr = 100.0
    val Zr = 108.883

    var xr = x / Xr
    var yr = y / Yr
    var zr = z / Zr

    val e = 0.008856
    val k = 903.3

    val fx = if ( xr > e ) {
        xr.pow(1/3)
    } else {
        ((k * xr) + 16 / 116.0)
    }

    val fy = if ( yr > e ) {
        yr.pow(1/3)
    } else {
        ((k * yr) + 16 / 116.0)
    }

    val fz = if ( zr > e ) {
        zr.pow(1 / 3)
    } else {
        ((k * zr) + 16 / 116.0)
    }

    return doubleArrayOf(
        (116 * fy) - 16,
        500 * (fx - fy),
        200 * (fy - fz)
    ).also {
        Log.d("SnapshotDiffing", "L: ${it[0]}, A: ${it[1]}, B: ${it[2]}")
    }
}

// CalculatesDelta E (CIE 1994) between two colors in the CIE LAB color space returning a value between 0-100 (0 means no difference, 100 means completely opposite)
// http://www.brucelindbloom.com/index.html?Eqn_DeltaE_CIE94.html
//
private fun Color.difference(other: Color): Double {
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
    ).also {
        Log.d("SnapshotDiffing", "ΔE: $it")
    }
}