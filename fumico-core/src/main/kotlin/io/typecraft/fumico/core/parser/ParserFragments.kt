package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.lib.parsecom.*

val HORIZONTAL_SPACES = listOf(" ", "\t")
val VERTICAL_SPACES = listOf("\r\n", "\n", "\r")


val skipHorizontalSpaces =
    skip(
        alt(
            tag(HORIZONTAL_SPACES[0]),
            *HORIZONTAL_SPACES.drop(1).map(::tag).toTypedArray()
        )
    )

val skipVerticalSpaces =
    skip(
        alt(
            tag(VERTICAL_SPACES[0]),
            *VERTICAL_SPACES.drop(1).map(::tag).toTypedArray()
        )
    )

val parseIdentifier by lazy {
    concat(
        mapResult(
            takeIf {
                when (it.toString()) {
                    in "0".."9" -> false
                    in HORIZONTAL_SPACES -> false
                    in VERTICAL_SPACES -> false
                    "\"", "=" -> false
                    else -> true
                }
            }
        ) {
            it.toString()
        },
        mapResult(many(takeIf {
            when (it.toString()) {
                in HORIZONTAL_SPACES -> false
                in VERTICAL_SPACES -> false
                "\"", "=" -> false
                else -> true
            }
        })) { it.joinToString("") }
    )
}