package com.quickbird.android_example

import androidx.navigation.NavController

sealed interface Destination
object Main : Destination
object Settings : Destination

val Destination.raw
    get() = this::class.simpleName ?: ""

fun NavController.navigate(destination: Destination) = navigate(destination.raw)
