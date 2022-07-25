package com.quickbird.snapshot.util

infix fun <A, B, C> ((A) -> B).compose(other: (B) -> C) = fun(a: A) = other(this(a))

infix fun <A, B, C> (suspend (A) -> B).compose(other: suspend (B) -> C): suspend (A) -> C = { a ->
    other(this(a))
}
