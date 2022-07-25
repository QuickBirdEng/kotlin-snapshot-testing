package com.quickbird.snapshot

fun <Value, Format, NewFormat> FileSnapshotting<Value, Format>.wrap(
    wrap: (Format) -> NewFormat,
    unwrap: (NewFormat) -> Format
) = FileSnapshotting(
    fileStoring = fileStoring.wrap(wrap, unwrap),
    snapshotting = snapshotting.wrap(wrap, unwrap)
)
