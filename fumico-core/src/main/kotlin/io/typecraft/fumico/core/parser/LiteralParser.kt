package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.functions.alt
import io.typecraft.parsecom.functions.mapResult


val parseIntegerLiteral =
    mapResult(
        token(Token.Kind.LiteralInteger)
    ) {
        Ast.Child.Expression.Literal.IntegerLiteral(it)
    }

val parseDecimalLiteral =
    mapResult(
        alt(
            token(Token.Kind.LiteralDecimal),
            token(Token.Kind.LiteralDecimalExponent)
        )
    ) { Ast.Child.Expression.Literal.DecimalLiteral(it) }

val parseStringLiteral =
    mapResult(
        token(Token.Kind.LiteralString)
    ) {
        Ast.Child.Expression.Literal.StringLiteral(it)
    }

val parseLiteral =
    alt(
        parseDecimalLiteral,
        parseIntegerLiteral,
        parseStringLiteral,
    )
