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
                skipHorizontalSpaces,
                skipVerticalSpaces,

                parseStatement,
                parseExpression,
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

    val (arguments, input2) = defaulting(
        many(
            preceded(
                skipHorizontalSpaces,
                parseBasicExpression,
            )
        ), emptyList()
    )(input1)

    val res = (listOf(expression).asSequence() + arguments.asSequence()).reduce { acc, right ->
        Ast.Child.Expression.FunctionCall(
            acc,
            right
        )
    }

    // TODO: infix, postfix operator

    ok(res, input2)
}

val parseBasicExpression: ParseFunction<Ast.Child.Expression> by lazy {
    alt(
        parseLiteral,
        parseName,
    )
}
