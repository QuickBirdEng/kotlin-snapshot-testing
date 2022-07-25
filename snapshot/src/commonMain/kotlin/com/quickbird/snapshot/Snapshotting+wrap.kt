package com.quickbird.snapshot

import com.quickbird.snapshot.util.compose

fun <Value, Format, NewFormat> Snapshotting<Value, Format>.wrap(
    wrap: (Format) -> NewFormat,
    unwrap: (NewFormat) -> Format
) = Snapshotting(
    diffing = diffing.wrap(wrap, unwrap),
    snapshot = snapshot compose wrap
)
