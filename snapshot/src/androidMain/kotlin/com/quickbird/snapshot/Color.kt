package com.quickbird.snapshot

import androidx.annotation.ColorInt
import kotlin.math.cbrt
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class Color(@ColorInt val value: Int) {
    fun equals(other: Color, any: Any) {

    }
}

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
    val lab1 = this.toLAB()
    val lab2 = other.toLAB()

    val deltaL = lab1[0] - lab2[0]
    val lBar = (lab1[0] + lab2[0]) / 2.0
    val c1 = sqrt(lab1[1].pow(2) + lab1[2].pow(2))
    val c2 = sqrt(lab2[1].pow(2) + lab2[2].pow(2))
    val cBar = (c1 + c2) / 2.0
    val deltaC = c1 - c2
    val deltaA = lab1[1] - lab2[1]
    val deltaB = lab1[2] - lab2[2]
    val deltaH = sqrt(deltaA.pow(2) + deltaB.pow(2) - deltaC.pow(2))

    val sl = 1.0
    val kc = 1.0
    val kh = 1.0
    val sc = 1.0 + 0.045 * cBar
    val sh = 1.0 + 0.015 * cBar

    return sqrt(
        (deltaL / sl).pow(2) +
                (deltaC / (kc * sc)).pow(2) +
                (deltaH / (kh * sh)).pow(2)
    )
}