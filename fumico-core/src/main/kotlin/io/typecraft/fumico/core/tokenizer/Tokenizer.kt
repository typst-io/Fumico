package io.typecraft.fumico.core.tokenizer

import io.typecraft.parsecom.functions.*

val tokenize: FumicoTokenizeFunction<List<Token>> by lazy {
    many(
        alt(
            tokenizeHorizontalSpaces,
            tokenizeVerticalSpace,

            tokenizeDecimalExponentLiteral,
            tokenizeDecimalLiteral,
            tokenizeIntegerLiteral,

            tokenizeStringLiteral,

            tokenizePunctuation,

            tokenizeIdentifierIdentifier,
            tokenizeIdentifierPrefix,
            tokenizeIdentifierInfix,
            tokenizeIdentifierPostfix,
        )
    )
}

val tokenizeHorizontalSpaces = createToken(
    Token.Kind.SpaceHorizontal,
    takeWhile1 {
        when (it) {
            ' ' -> true
            '\t' -> true
            else -> false
        }
    }
) {
    it.joinToString("")
}

val tokenizeVerticalSpace: FumicoTokenizeFunction<Token> = mapMeta(
    createToken(
        Token.Kind.SpaceVertical,
        alt(
            tag('\r', '\n'),
            tag('\r'),
            tag('\n'),
        )
    ) {
        it.joinToString("")
    }
) {
    it.copy(line = it.line + 1, lastOffset = start)
}

val tokenizeDecimalExponentLiteral: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.LiteralDecimalExponent,
        concatWithFlatten(
            takeWhile1 { it in '0'..'9' },
            defaulting(
                concatWithFlatten(
                    tag('.'),
                    takeWhile1 { it in '0'..'9' },
                ),
                emptyList()
            ),
            alt(tag('e'), tag('E')),
            alt(tag('-'), tag('+'), returning(emptyList())),
            takeWhile1 { it in '0'..'9' },
        )
    ) {
        it.joinToString("")
    }

val tokenizeDecimalLiteral: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.LiteralDecimal,
        concatWithFlatten(
            takeWhile1 { it in '0'..'9' },
            tag('.'),
            takeWhile1 { it in '0'..'9' }
        )
    ) {
        it.joinToString("")
    }

val tokenizeIntegerLiteral: FumicoTokenizeFunction<Token> =
    createToken(Token.Kind.LiteralInteger,
        takeWhile1 { it in '0'..'9' }
    ) {
        it.joinToString("")
    }

val tokenizeStringLiteral: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.LiteralString,
        concatWithFlatten(
            tag('"'),
            flatten(many(alt(
                concatWithFlatten(
                    tag('\\'),
                    alt(
                        tag('\"'),
                        tag('n'),
                        tag('t'),
                        tag('r'),
                        tag('\\'),
                    )
                ),
                mapResult(takeIf {
                    when (it) {
                        '\"', '\\', '\r', '\n' -> false
                        else -> true
                    }
                }) { listOf(it) }
            ))),
            tag('"'),
        )
    ) {
        it.joinToString("")
    }

val punctuationMap = mapOf(
    '!' to Token.Kind.PunctuationExclamationMark,
    '#' to Token.Kind.PunctuationNumberSign,
    '$' to Token.Kind.PunctuationDollarSign,
    '%' to Token.Kind.PunctuationPercentSign,
    '&' to Token.Kind.PunctuationAmpersand,
    '*' to Token.Kind.PunctuationAsterisk,
    '+' to Token.Kind.PunctuationPlusSign,
    ',' to Token.Kind.PunctuationComma,
    '-' to Token.Kind.PunctuationHyphenMinus,
    '.' to Token.Kind.PunctuationFullStop,
    '/' to Token.Kind.PunctuationSolidus,
    ':' to Token.Kind.PunctuationColon,
    ';' to Token.Kind.PunctuationSemicolon,
    '<' to Token.Kind.PunctuationLessThanSign,
    '=' to Token.Kind.PunctuationEqualsSign,
    '>' to Token.Kind.PunctuationGreaterThanSign,
    '?' to Token.Kind.PunctuationQuestionMark,
    '@' to Token.Kind.PunctuationCommercialAt,
    '\\' to Token.Kind.PunctuationReverseSolidus,
    '^' to Token.Kind.PunctuationCircumflexAccent,
    '|' to Token.Kind.PunctuationVerticalLine,
    '~' to Token.Kind.PunctuationTilde,
    '(' to Token.Kind.PunctuationLeftParenthesis,
    '[' to Token.Kind.PunctuationLeftSquareBracket,
    '{' to Token.Kind.PunctuationLeftCurlyBracket,
    ')' to Token.Kind.PunctuationRightParenthesis,
    ']' to Token.Kind.PunctuationRightSquareBracket,
    '}' to Token.Kind.PunctuationRightCurlyBracket,
)

val tokenizePunctuation: FumicoTokenizeFunction<Token> =
    alt(punctuationMap.map { (ch, kind) -> createToken(kind, isA(ch)) { it.toString() } })

val tokenizePunctuations: FumicoTokenizeFunction<List<Char>> by lazy {
    takeWhile1 { it in punctuationMap }
}

val tokenizeIdentifierIdentifier: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.IdentifierIdentifier,
        filter(
            mapResult(
                concatWithFlatten(
                    mapResult(takeIf {
                        when (it) {
                            in punctuationMap -> false
                            in '0'..'9' -> false
                            ' ', '\t', '\r', '\n', '_' -> false
                            else -> true
                        }
                    }) {
                        listOf(it)
                    },
                    takeWhile {
                        when (it) {
                            in punctuationMap -> false
                            ' ', '\t', '\r', '\n' -> false
                            else -> true
                        }
                    }
                )
            ) {
                it.joinToString("")
            }
        ) {
            when (it) {
                "prefix", "infix", "postfix" -> false
                else -> true
            }
        }
    ) {
        it
    }

val tokenizeIdentifierPrefix: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.IdentifierPrefix,
        concatWithFlatten(
            tag("prefix ".toList()),
            tokenizePunctuations,
        )
    ) {
        it.joinToString("")
    }

val tokenizeIdentifierInfix: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.IdentifierInfix,
        concatWithFlatten(
            tag("infix ".toList()),
            tokenizePunctuations,
        )
    ) {
        it.joinToString("")
    }

val tokenizeIdentifierPostfix: FumicoTokenizeFunction<Token> =
    createToken(
        Token.Kind.IdentifierPostfix,
        concatWithFlatten(
            tag("postfix ".toList()),
            tokenizePunctuations,
        )
    ) {
        it.joinToString("")
    }
