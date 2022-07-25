package com.quickbird.snapshot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot

fun <Format> Snapshotting<SemanticsNodeInteraction, Format>.pullBackToTestRule() =
    pullback { rule: AndroidComposeTestRule<*, *> -> rule.onRoot() }
