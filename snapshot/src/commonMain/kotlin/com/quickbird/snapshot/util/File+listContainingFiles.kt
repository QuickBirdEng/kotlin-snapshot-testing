package com.quickbird.snapshot.util

import java.io.File

fun File.listContainingFiles(): List<File> = listFiles()?.toList() ?: emptyList()
