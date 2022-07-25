package com.quickbird.snapshot

fun <Value, NewValue, Format> FileSnapshotting<Value, Format>.pullback(
    transform: suspend (NewValue) -> Value
): FileSnapshotting<NewValue, Format> = FileSnapshotting(
    snapshotting = snapshotting.pullback(transform),
    fileStoring = fileStoring
)
