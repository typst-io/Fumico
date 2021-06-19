package io.typecraft.fumico.core.parser

import arrow.core.getOrHandle
import arrow.core.handleError
import arrow.core.handleErrorWith
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*


val parseRoot: ParseFunction<Ast.Root> by lazy {
    mapResult(
        many(
            preceded(
                skipAllSpaces,
                alt(
                    parseStatement,
                    parseExpression,
                )
            )
        )
    ) {
        Ast.Root(it)
    }
}


val parseStatement: ParseFunction<Ast.Child.Statement> by lazy {
    alt(
        parseFunctionDeclaration,
        parsePrefixOperatorFunctionDeclaration,
        parseInfixOperatorFunctionDeclaration,
        parsePostfixOperatorFunctionDeclaration,
    )
}

val parseExpression: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (expression, input1) = parsePostfixOperatorExpression(input).getOrHandle { return@body err(it) }

    val (arguments, input2) = defaulting(
        many(
            preceded(
                skipHorizontalSpaces,
                parseInfixOperatorExpressionWithPrecedence,
            )
        ),
        emptyList()
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

val parsePostfixOperatorExpression: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (expressionRaw, input3) = parseInfixOperatorExpressionWithPrecedence(input).getOrHandle { return@body err(it) }

    val (_, input4) = opt(skipHorizontalSpaces)(input3)

    val (postfixOperators, input5) = defaulting(
        separatedList(parseSpecialIdentifier, skipHorizontalSpaces),
        emptyList()
    )(input4)

    val expression = postfixOperators.fold(expressionRaw) { acc, operator ->
        Ast.Child.Expression.FunctionCall(Ast.Child.Expression.Name("postfix $operator"), acc)
    }

    ok(expression, input5)
}

val parseInfixOperatorExpressionWithPrecedence: ParseFunction<Ast.Child.Expression> by lazy {
    createInfixOperatorParser(
        charArrayOf('$'),
        createInfixOperatorParser(
            charArrayOf('*', '/', '%'),
            createInfixOperatorParser(
                charArrayOf('+', '-'),
                createInfixOperatorParser(
                    charArrayOf(':'),
                    createInfixOperatorParser(
                        charArrayOf('=', '!'),
                        createInfixOperatorParser(
                            charArrayOf('<', '>'),
                            createInfixOperatorParser(
                                charArrayOf('&'),
                                createInfixOperatorParser(
                                    charArrayOf('^'),
                                    createInfixOperatorParser(
                                        charArrayOf('|'), parsePrefixOperatorExpression,
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}


val RIGHT_ASSOCIATIVE_LAST_CHARS = charArrayOf(':', '$')

fun createInfixOperatorParser(
    begin: CharArray,
    child: ParseFunction<Ast.Child.Expression>
): ParseFunction<Ast.Child.Expression> {
    fun parse(
        input: ParseInput,
        lhs: Ast.Child.Expression?,
        rightAssociative: Boolean
    ): ParseResult<Ast.Child.Expression> {
        if (lhs == null) {
            val (newLhs, input1) = child(input).getOrHandle { return err(it) }
            return parse(
                input1,
                newLhs,
                rightAssociative,
            )
        }
        val (_, input1) = opt(skipHorizontalSpaces)(input)
        val (operator, input2) = filter(parseSpecialIdentifier) { s ->
            s.firstOrNull()?.let { it in begin } == true && s.lastOrNull()
                ?.let { it in RIGHT_ASSOCIATIVE_LAST_CHARS } == rightAssociative
        }(input1).getOrHandle {
            return if (rightAssociative) {
                err(it)
            } else {
                ok(lhs, input)
            }
        }
        val (_, input4) = opt(skipHorizontalSpaces)(input2)
        val (rhs, input5) = if (rightAssociative) {
            parse(input4, null, rightAssociative).handleErrorWith { child(input4) }
        } else {
            child(input4)
        }.getOrHandle {
            return if (rightAssociative) {
                err(it)
            } else {
                ok(lhs, input)
            }
        }

        val expr = Ast.Child.Expression.FunctionCall(
            Ast.Child.Expression.FunctionCall(
                Ast.Child.Expression.Name("infix $operator"),
                lhs
            ),
            rhs
        )

        return if (rightAssociative) {
            ok(
                expr,
                input5
            )
        } else {
            parse(input5, expr, rightAssociative)
        }
    }

    return { input ->
        parse(input, null, true).handleErrorWith {
            parse(input, null, false)
        }.handleErrorWith {
            child(input)
        }
    }
}

val parsePrefixOperatorExpression: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (prefixOperators, input1) = defaulting(
        separatedList(parseSpecialIdentifier, skipHorizontalSpaces),
        emptyList()
    )(input)

    val (_, input2) = opt(skipHorizontalSpaces)(input1)

    val (expressionRaw, input3) = parseBasicExpression(input2).getOrHandle { return@body err(it) }

    val (_, input4) = opt(skipHorizontalSpaces)(input3)

    val expression = prefixOperators.fold(expressionRaw) { acc, operator ->
        Ast.Child.Expression.FunctionCall(Ast.Child.Expression.Name("prefix $operator"), acc)
    }

    ok(expression, input4)
}

val parseBasicExpression: ParseFunction<Ast.Child.Expression> by lazy {
    alt(
        parseLiteral,
        parseName,
    )
}
