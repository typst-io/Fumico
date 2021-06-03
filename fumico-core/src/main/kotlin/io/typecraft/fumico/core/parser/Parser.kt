package io.typecraft.fumico.core.parser

import arrow.core.getOrHandle
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.math.BigDecimal
import java.math.BigInteger

val parseRoot: ParseFunction<Ast.Root> by lazy {
    mapResult(
        many(
            alt(
                parseStatement,
                parseExpression,
                skipHorizontalSpaces,
                skipVerticalSpaces
            )
        )
    ) {
        Ast.Root(it.filterNotNull())
    }
}


val parseStatement: ParseFunction<Ast.Child.Statement> by lazy {
    alt(
        parseFunctionDeclaration,
    )
}

val parseExpression: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (expression, input1) = parseBasicExpression(input).getOrHandle { return@body err(it) }

    // TODO: function call and infix function call

    ok(expression, input1)
}

val parseBasicExpression: ParseFunction<Ast.Child.Expression> by lazy {
    alt(
        parseLiteral,
    )
}
