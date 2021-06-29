package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.functions.*

val skipVerticalSpaces = opt(many(token(Token.Kind.SpaceVertical)))

val skipHorizontalSpaces = opt(many(token(Token.Kind.SpaceHorizontal)))

val skipAllSpaces =
    opt(
        many(
            alt(token(Token.Kind.SpaceVertical), token(Token.Kind.SpaceHorizontal))
        )
    )


val parseIdentifier = token(Token.Kind.IdentifierIdentifier)

val parseSpecialIdentifier: FumicoParseFunction<Token> = mapResult(takeWhile1 {
    when (it.kind) {
        Token.Kind.PunctuationExclamationMark,
        Token.Kind.PunctuationNumberSign,
        Token.Kind.PunctuationDollarSign,
        Token.Kind.PunctuationPercentSign,
        Token.Kind.PunctuationAmpersand,
        Token.Kind.PunctuationAsterisk,
        Token.Kind.PunctuationPlusSign,
        Token.Kind.PunctuationHyphenMinus,
        Token.Kind.PunctuationFullStop,
        Token.Kind.PunctuationSolidus,
        Token.Kind.PunctuationColon,
        Token.Kind.PunctuationSemicolon,
        Token.Kind.PunctuationLessThanSign,
        Token.Kind.PunctuationEqualsSign,
        Token.Kind.PunctuationGreaterThanSign,
        Token.Kind.PunctuationQuestionMark,
        Token.Kind.PunctuationCommercialAt,
        Token.Kind.PunctuationCircumflexAccent,
        Token.Kind.PunctuationVerticalLine,
        Token.Kind.PunctuationTilde,
        Token.Kind.PunctuationLeftSquareBracket,
        Token.Kind.PunctuationLeftCurlyBracket,
        Token.Kind.PunctuationRightSquareBracket,
        Token.Kind.PunctuationRightCurlyBracket -> true
        else -> false
    }
}) {
    it.reduce { l, r ->
        l.mergeWith(Token.Kind.Punctuations, r)
    }
}

val parsePrefixOperatorIdentifier = token(Token.Kind.IdentifierPrefix)

val parseInfixOperatorIdentifier = token(Token.Kind.IdentifierInfix)

val parsePostfixOperatorIdentifier = token(Token.Kind.IdentifierPostfix)
