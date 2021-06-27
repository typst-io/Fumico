package io.typecraft.fumico.core.tokenizer

data class Token(val kind: Kind, val visual: String, val actual: String, val span: Span) {
    constructor(kind: Kind, text: String, span: Span) : this(kind, text, text, span)

    enum class Kind {
        SpaceHorizontal,
        SpaceVertical,

        IdentifierIdentifier,
        IdentifierPlaceholderName,
        IdentifierPrefix,
        IdentifierInfix,
        IdentifierPostfix,

        Punctuations,

        PunctuationExclamationMark,
        PunctuationNumberSign,
        PunctuationDollarSign,
        PunctuationPercentSign,
        PunctuationAmpersand,
        PunctuationAsterisk,
        PunctuationPlusSign,
        PunctuationComma,
        PunctuationHyphenMinus,
        PunctuationFullStop,
        PunctuationSolidus,
        PunctuationColon,
        PunctuationSemicolon,
        PunctuationLessThanSign,
        PunctuationEqualsSign,
        PunctuationGreaterThanSign,
        PunctuationQuestionMark,
        PunctuationCommercialAt,
        PunctuationReverseSolidus,
        PunctuationCircumflexAccent,
        PunctuationVerticalLine,
        PunctuationTilde,
        PunctuationLeftParenthesis,
        PunctuationLeftSquareBracket,
        PunctuationLeftCurlyBracket,
        PunctuationRightParenthesis,
        PunctuationRightSquareBracket,
        PunctuationRightCurlyBracket,

        LiteralInteger,
        LiteralDecimal,
        LiteralDecimalExponent,
        LiteralString,

        Error,
    }

    fun mergeWith(newKind: Kind, other: Token): Token {
        val (min, max) = if (this.span < other.span) {
            Pair(this, other)
        } else {
            Pair(other, this)
        }

        return Token(
            newKind,
            min.visual + max.visual,
            min.actual + max.actual,
            Span(min.span.begin, max.span.end)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (kind != other.kind) return false
        if (actual != other.actual) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind.hashCode()
        result = 31 * result + actual.hashCode()
        return result
    }

    override fun toString(): String = "$kind(visual=$visual, actual=$actual)"
}
