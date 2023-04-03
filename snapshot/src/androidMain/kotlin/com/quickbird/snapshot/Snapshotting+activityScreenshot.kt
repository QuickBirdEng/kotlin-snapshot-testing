package com.quickbird.snapshot

import android.app.Activity

val Snapshotting.Companion.activityScreenshot
    get() = viewScreenshot.pullback { activity: Activity ->
        activity.window.decorView.rootView
    }
