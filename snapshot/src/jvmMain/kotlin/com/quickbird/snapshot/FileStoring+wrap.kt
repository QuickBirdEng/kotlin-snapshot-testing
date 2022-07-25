package com.quickbird.snapshot

import com.quickbird.snapshot.util.compose

fun <Value, NewValue> FileStoring<Value>.wrap(
    wrap: (Value) -> NewValue,
    unwrap: (NewValue) -> Value
) = FileStoring(
    store = { value: NewValue, file -> store(unwrap(value), file) },
    load = load compose wrap,
    fileExtension = fileExtension
)
