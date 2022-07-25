package com.quickbird.snapshot

fun <Value, NewValue> Diffing<Value>.wrap(wrap: (Value) -> NewValue, unwrap: (NewValue) -> Value) =
    Diffing<NewValue> { value, value2 -> invoke(unwrap(value), unwrap(value2))?.let(wrap) }
