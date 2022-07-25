package com.quickbird.snapshot

fun <Value, NewValue, Format> Snapshotting<Value, Format>.pullback(
    transform: suspend (NewValue) -> Value
): Snapshotting<NewValue, Format> = Snapshotting(
    diffing = diffing,
    snapshot = { newValue -> snapshot(transform(newValue)) }
)
