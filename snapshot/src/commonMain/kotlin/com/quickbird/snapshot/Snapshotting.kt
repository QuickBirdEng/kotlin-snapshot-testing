package com.quickbird.snapshot

import com.quickbird.snapshot.util.identity

data class Snapshotting<Value, Format>(
    val diffing: Diffing<Format>,
    val snapshot: suspend (Value) -> Format
) {
    companion object
}

fun <Value> Snapshotting(
    diffing: Diffing<Value>
) = Snapshotting(
    diffing = diffing,
    snapshot = ::identity
)
