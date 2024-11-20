package com.quickbird.snapshot

import android.graphics.ColorSpace
import android.graphics.Color as AndroidColor
import androidx.annotation.ColorInt
import kotlin.collections.component1
import kotlin.math.abs
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
/**
 * Convert the color to the CIE LAB color space using sRGB color space and D65 white reference white
 */
private fun Color.toLAB(): FloatArray {
    val labConnector = ColorSpace.connect(
        ColorSpace.get(ColorSpace.Named.SRGB),
        ColorSpace.get(ColorSpace.Named.CIE_LAB)
    )

    val rgb = floatArrayOf(
        AndroidColor.red(value) / 255.0f,
        AndroidColor.green(value) / 255.0f,
        AndroidColor.blue(value) / 255.0f
    )

    return labConnector.transform(rgb[0], rgb[1], rgb[2])
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