package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.math.BigInteger

val parseRoot: ParseFunction<Ast.Root> by lazy {
    mapResult(many(parseExpression)) {
        Ast.Root(it)
    }
}

val parseExpression: ParseFunction<Ast.Expression> by lazy {
    alt(
        parseLiteral,
    )
}

val parseLiteral: ParseFunction<Ast.Expression.Literal> by lazy {
    alt(
        parseIntegerLiteral,
    )
}

val parseIntegerLiteral: ParseFunction<Ast.Expression.Literal.IntegerLiteral> by lazy {
    mapResult(takeWhile1 { it in '0'..'9' }) {
        Ast.Expression.Literal.IntegerLiteral(BigInteger(it.toString()))
    }
}
