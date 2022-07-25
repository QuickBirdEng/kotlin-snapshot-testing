package com.quickbird.jvm_example

data class QuickBird(
    val shape: String = "Potato",
    val numberOfEatenWorms: Int = 42
) {
    override fun toString() = """
            QuickBird
                shape = $shape
                numberOfEatenWorms = $numberOfEatenWorms
        """.trimIndent()
}
