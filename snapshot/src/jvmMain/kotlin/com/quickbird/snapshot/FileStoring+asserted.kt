package com.quickbird.snapshot

val <Value> FileStoring<Value>.asserted
    get() = copy(
        store = { value, file ->
            try {
                store(value, file)
            } catch (error : Throwable) {
                throw AssertionError("Couldn't store value: $value to file: $file")
            }
        },
        load = { file ->
            try {
                load(file)
            } catch (error: Throwable) {
                throw AssertionError("Couldn't load file: $file")
            }
        }
    )
