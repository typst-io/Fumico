package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.math.BigInteger

val parseRoot: ParseFunction<Ast.Root> by lazy {
    mapResult(many(alt(parseExpression, skipHorizontalSpaces, skipVerticalSpaces))) {
        Ast.Root(it.filterNotNull())
    }
}

val skipHorizontalSpaces =
    mapResult(
        alt(
            tag(" "),
            tag("\t"),
        )
    ) { null }

val skipVerticalSpaces =
    mapResult(
        alt(
            tag("\r\n"),
            tag("\n"),
            tag("\r"),
        )
    ) { null }

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
