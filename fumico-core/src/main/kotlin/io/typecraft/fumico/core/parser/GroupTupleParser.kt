package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.functions.*

val parseGroupTuple: FumicoParseFunction<Ast.Child.Expression> by lazy {
    mapResult(
        tuple(
            token(Token.Kind.PunctuationLeftParenthesis),
            defaulting(
                separatedList(
                    preceded(skipAllSpaces, ::parseExpression),
                    preceded(skipAllSpaces, token(Token.Kind.PunctuationComma))
                ),
                emptyList()
            ),
            opt(preceded(skipAllSpaces, token(Token.Kind.PunctuationComma))),
            failure(token(Token.Kind.PunctuationRightParenthesis)) {
                "Expected `)` but `${it.peek()?.visual ?: "EOF"}` found"
            },
        ),
    ) { (_, expressions, lastComma, _) ->
        if (lastComma == null && expressions.size == 1) {
            expressions[0]
        } else {
            Ast.Child.Expression.Tuple(expressions)
        }
    }
}