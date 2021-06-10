package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.lib.parsecom.*

val HORIZONTAL_SPACES = listOf(" ", "\t")
val VERTICAL_SPACES = listOf("\r\n", "\n", "\r")

val KEYWORDS = listOf(
    "prefix",
    "infix",
    "postfix",
)

val SPECIAL_CHARACTERS = listOf(
    '!',
    '#',
    '$',
    '%',
    '&',
    '*',
    '+',
    '-',
    '.',
    ':',
    ';',
    '<',
    '=',
    '>',
    '?',
    '@',
    '^',
    '~',
)


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
    filter(concat(
        mapResult(
            takeIf {
                when {
                    it in '0'..'9' -> false
                    it.toString() in HORIZONTAL_SPACES -> false
                    it.toString() in VERTICAL_SPACES -> false
                    it in SPECIAL_CHARACTERS -> false
                    it.toString() == "\"" -> false
                    it.toString() == "=" -> false
                    else -> true
                }
            }
        ) {
            it.toString()
        },
        mapResult(many(takeIf {
            when {
                it.toString() in HORIZONTAL_SPACES -> false
                it.toString() in VERTICAL_SPACES -> false
                it in SPECIAL_CHARACTERS -> false
                it.toString() == "\"" -> false
                it.toString() == "=" -> false
                else -> true
            }
        })) { it.joinToString("") }
    )) {
        it !in KEYWORDS
    }
}

val parseSpecialIdentifier by lazy {
    concat(
        takeWhile1 {
            it in SPECIAL_CHARACTERS
        }
    )
}

val parsePrefixOperatorIdentifier by lazy {
    concat(tag("prefix "), parseSpecialIdentifier)
}

val parseInfixOperatorIdentifier by lazy {
    concat(tag("infix "), parseSpecialIdentifier)
}

val parsePostfixOperatorIdentifier by lazy {
    concat(tag("postfix "), parseSpecialIdentifier)
}