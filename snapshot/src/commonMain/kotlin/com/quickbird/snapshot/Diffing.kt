package com.quickbird.snapshot

fun interface Diffing<Value> : (Value, Value) -> Value? {
    companion object
}
