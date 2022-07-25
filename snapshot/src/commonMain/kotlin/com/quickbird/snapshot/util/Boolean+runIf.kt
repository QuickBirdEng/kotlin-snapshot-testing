package com.quickbird.snapshot.util

inline fun <T> Boolean.runIfTrue(block: () -> T): T? {
    return if (this) block() else null
}

inline fun <T> Boolean.runIfFalse(block: () -> T): T? {
    return not().runIfTrue(block)
}
