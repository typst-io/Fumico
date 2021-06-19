package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.math.BigDecimal
import java.math.BigInteger

val parseLiteral: ParseFunction<Ast.Child.Expression.Literal> by lazy {
    alt(
        parseDecimalLiteral,
        parseIntegerLiteral,
        parseStringLiteral,
    )
}

val parseIntegerLiteral: ParseFunction<Ast.Child.Expression.Literal.IntegerLiteral> by lazy {
    mapResult(takeWhile1 { it in '0'..'9' }) {
        Ast.Child.Expression.Literal.IntegerLiteral(BigInteger(it))
    }
}

val parseDecimalLiteral: ParseFunction<Ast.Child.Expression.Literal.DecimalLiteral> by lazy {
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
        Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal(it))
    }
}

val parseStringLiteral: ParseFunction<Ast.Child.Expression.Literal.StringLiteral> by lazy {
    mapResult(
        delimited(
            tag("\""),
            many(alt(
                concat(
                    tag("\\"),
                    alt(
                        tag("\""),
                        tag("n"),
                        tag("t"),
                        tag("r"),
                        tag("\\"),
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
        Ast.Child.Expression.Literal.StringLiteral(
            it.joinToString("") { s ->
                when (s) {
                    "\\\"" -> "\""
                    "\\n" -> "\n"
                    "\\t" -> "\t"
                    "\\r" -> "\r"
                    "\\\\" -> "\\"
                    else -> s
                }
            },
            it.joinToString("")
        )
    }
}