package io.typecraft.fumico.core.parser

import arrow.core.getOrHandle
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.math.BigDecimal
import java.math.BigInteger

val parseRoot: ParseFunction<Ast.Root> by lazy {
    mapResult(many(alt(parseExpression, skipHorizontalSpaces, skipVerticalSpaces))) {
        Ast.Root(it.filterNotNull())
    }
}

val skipHorizontalSpaces =
    skip(
        alt(
            tag(" "),
            tag("\t"),
        )
    )

val skipVerticalSpaces =
    skip(
        alt(
            tag("\r\n"),
            tag("\n"),
            tag("\r"),
        )
    )

val parseExpression: ParseFunction<Ast.Expression> = body@{ input ->
    val (expression, input1) = parseBasicExpression(input).getOrHandle { return@body err(it) }

    // TODO: function call and infix function call

    ok(expression, input1)
}

val parseBasicExpression: ParseFunction<Ast.Expression> by lazy {
    alt(
        parseLiteral,
    )
}

val parseLiteral: ParseFunction<Ast.Expression.Literal> by lazy {
    alt(
        parseDecimalLiteral,
        parseIntegerLiteral,
        parseStringLiteral,
    )
}

val parseIntegerLiteral: ParseFunction<Ast.Expression.Literal.IntegerLiteral> by lazy {
    mapResult(takeWhile1 { it in '0'..'9' }) {
        Ast.Expression.Literal.IntegerLiteral(BigInteger(it))
    }
}

val parseDecimalLiteral: ParseFunction<Ast.Expression.Literal.DecimalLiteral> by lazy {
    mapResult(
        alt(
            concat(
                takeWhile1 { it in '0'..'9' },
                tag("."),
                takeWhile1 { it in '0'..'9' },
            ),
            concat(
                takeWhile1 { it in '0'..'9' },
                alt(tag("e"), tag("E")),
                alt(tag("-"), tag("+"), returning("+")),
                takeWhile1 { it in '0'..'9' }
            )
        )
    ) {
        Ast.Expression.Literal.DecimalLiteral(BigDecimal(it))
    }
}

val parseStringLiteral: ParseFunction<Ast.Expression.Literal.StringLiteral> by lazy {
    mapResult(
        delimited(
            tag("\""),
            many(alt(
                preceded(
                    tag("\\"),
                    alt(
                        mapResult(tag("\"")) { "\"" },
                        mapResult(tag("n")) { "\n" },
                        mapResult(tag("t")) { "\t" },
                        mapResult(tag("r")) { "\r" },
                        mapResult(tag("\\")) { "\\" },
                    )
                ),
                mapResult(takeIf {
                    when (it) {
                        '\"', '\\', '\n' -> false
                        else -> true
                    }
                }) { it.toString() }
            )),
            tag("\"")
        )
    ) {
        Ast.Expression.Literal.StringLiteral(it.joinToString(""))
    }
}