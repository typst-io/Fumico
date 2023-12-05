package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.functions.mapResult
import io.typecraft.parsecom.functions.opt
import io.typecraft.parsecom.functions.preceded
import io.typecraft.parsecom.functions.tuple

val parseIf by lazy {
    mapResult(
        tuple(
            token(Token.Kind.KeywordIf),
            skipHorizontalSpaces,
            ::parseExpression,
            skipHorizontalSpaces,
            token(Token.Kind.PunctuationHyphenMinus),
            token(Token.Kind.PunctuationGreaterThanSign),
            skipAllSpaces,
            ::parseExpression,
            opt(
                tuple(
                    skipAllSpaces,
                    token(Token.Kind.KeywordElse),
                    skipHorizontalSpaces,
                    token(Token.Kind.PunctuationHyphenMinus),
                    token(Token.Kind.PunctuationGreaterThanSign),
                    skipAllSpaces,
                    ::parseExpression,
                )
            )
        )
    ) { (_, _, condition, _, _, _, _, then, otherwise) ->
        Ast.Child.Expression.If(condition, then, otherwise?.seventh)
    }
}