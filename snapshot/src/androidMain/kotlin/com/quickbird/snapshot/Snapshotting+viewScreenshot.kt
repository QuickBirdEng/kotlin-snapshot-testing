package com.quickbird.snapshot

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

val Snapshotting.Companion.viewScreenshot
    get() = Snapshotting(
        diffing = Diffing.bitmap(colorDiffing = Diffing.intMean),
        snapshot = { view: View ->
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).apply {
                view.draw(Canvas(this))
            }
        }
    )
