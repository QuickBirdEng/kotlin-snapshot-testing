@file:Suppress("NAME_SHADOWING")

package com.quickbird.snapshot.androidTransfer

fun runAdb(vararg commands: String, deviceId: String? = null): Int {
    val commands = if (deviceId == null) commands else arrayOf("-s", deviceId, *commands)
    return ProcessBuilder("adb", *commands).start().waitFor()
}

fun runAdbShell(vararg commands: String, appBundle: String) =
    runAdb("shell", "run-as", appBundle, *commands)

fun listAdbDevices() = ProcessBuilder("adb", "devices")
    .start()
    .inputStream
    .bufferedReader()
    .lineSequence()
    .toList()
    .filter { line -> line.endsWith("device") }
    .map { it.split("\t").first() }
