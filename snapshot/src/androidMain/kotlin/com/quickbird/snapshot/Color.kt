package com.quickbird.snapshot

import androidx.annotation.ColorInt

data class Color(@ColorInt val value: Int)

val @receiver:ColorInt Int.color
    get() = Color(this)
