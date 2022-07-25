package com.quickbird.snapshot

val Diffing.Companion.lines
    get() = Diffing { first: String, second: String ->
        if (first == second) null
        else first
            .split("\n")
            .zip(second.split("\n"))
            .joinToString(separator = "\n") { (first, second) ->
                if (first == second) first
                else "-$first\n+$second"
            }
    }
