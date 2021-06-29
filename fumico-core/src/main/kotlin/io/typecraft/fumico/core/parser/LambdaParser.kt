package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.functions.defaulting
import io.typecraft.parsecom.functions.mapResult
import io.typecraft.parsecom.functions.separatedList
import io.typecraft.parsecom.functions.tuple

val parseLambda = mapResult(
    tuple(
        defaulting(
            mapResult(
                tuple(
                    token(Token.Kind.PunctuationReverseSolidus),
                    skipHorizontalSpaces,
                    separatedList(parseIdentifier, skipHorizontalSpaces),
                    skipHorizontalSpaces,
                )
            ) {
                it.third
            },
            emptyList()
        ),
        token(Token.Kind.PunctuationHyphenMinus),
        token(Token.Kind.PunctuationGreaterThanSign),
        ::parseExpression,
    )
) { (arguments, _, _, body) ->
    Ast.Child.Expression.Lambda(arguments, body)
}