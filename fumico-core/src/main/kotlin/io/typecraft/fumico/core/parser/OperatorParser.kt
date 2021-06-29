package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.ParseResult
import io.typecraft.parsecom.functions.*

val parsePostfixOperatorExpression: FumicoParseFunction<Ast.Child.Expression> by lazy {
    mapResult(
        tuple(
            parseInfixOperatorExpressionWithPrecedence,
            skipHorizontalSpaces,
            defaulting(
                separatedList(parseSpecialIdentifier, skipHorizontalSpaces),
                emptyList()
            )
        )
    ) { (expression, _, postfixOperators) ->
        postfixOperators.fold(expression) { acc, operator ->
            Ast.Child.Expression.FunctionCall(
                Ast.Child.Expression.Name(
                    operator.copy(
                        kind = Token.Kind.IdentifierPostfix,
                        actual = "postfix ${operator.actual}"
                    )
                ), acc
            )
        }
    }
}

val parseInfixOperatorExpressionWithPrecedence: FumicoParseFunction<Ast.Child.Expression> by lazy {
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
    child: FumicoParseFunction<Ast.Child.Expression>
): FumicoParseFunction<Ast.Child.Expression> {
    tailrec fun parse(
        input: FumicoParseInput,
        inputLhs: FumicoParseInput,
        acc: List<Pair<Ast.Child.Expression, Token>>
    ): FumicoParseResult<Ast.Child.Expression> {
        fun buildResult(
            acc1: List<Pair<Ast.Child.Expression, Token?>>,
            input1: FumicoParseInput
        ): FumicoParseResult<Ast.Child.Expression> {
            val stack = acc1.fold(emptyList<Pair<Ast.Child.Expression, Token?>>()) { stack, (expr, operator) ->
                val isRightAssociative =
                    stack.lastOrNull()?.second?.actual?.lastOrNull()?.let { it in RIGHT_ASSOCIATIVE_LAST_CHARS } == true

                if (stack.isEmpty() || isRightAssociative) {
                    stack + Pair(expr, operator?.let {
                        it.copy(kind = Token.Kind.IdentifierInfix, actual = "infix ${it.actual}")
                    })
                } else {
                    val (currentLhs, currentOperator) = stack.last()
                    stack.dropLast(1) + Pair(
                        Ast.Child.Expression.FunctionCall(
                            Ast.Child.Expression.FunctionCall(
                                Ast.Child.Expression.Name(currentOperator!!),
                                currentLhs,
                            ),
                            expr,
                        ),
                        operator,
                    )
                }
            }

            val reduced = stack.reduce { l, r ->
                Pair(
                    Ast.Child.Expression.FunctionCall(
                        Ast.Child.Expression.FunctionCall(
                            Ast.Child.Expression.Name(l.second!!),
                            l.first,
                        ),
                        r.first,
                    ),
                    r.second,
                )
            }.first

            return ParseResult.Ok(reduced, input1)
        }

        val (lhs, input1) = child(input).unwrapOr {
            return if (acc.isEmpty()) {
                it.into()
            } else {
                buildResult(acc, inputLhs)
            }
        }
        val (_, input2) = skipHorizontalSpaces(input1)
        val (operator, input3) = filter(parseSpecialIdentifier) { s ->
            s.actual.firstOrNull()?.let { it in begin } == true
        }(input2).unwrapOr {
            return buildResult(acc + Pair(lhs, null), input1)
        }


        return parse(input3, input1, acc + Pair(lhs, operator))
    }

    return { input ->
        parse(input, input, emptyList())
    }
}


val parsePrefixOperatorExpression: FumicoParseFunction<Ast.Child.Expression> by lazy {
    mapResult(
        tuple(
            defaulting(
                separatedList(
                    filter(parseSpecialIdentifier) {
                        it.actual != "->"
                    },
                    skipHorizontalSpaces
                ),
                emptyList()
            ),
            skipHorizontalSpaces,
            parseBasicExpression,
        )
    ) { (prefixOperators, _, expression) ->
        prefixOperators.fold(expression) { acc, operator ->
            Ast.Child.Expression.FunctionCall(
                Ast.Child.Expression.Name(
                    operator.copy(
                        kind = Token.Kind.IdentifierPrefix,
                        actual = "prefix ${operator.actual}"
                    )
                ), acc
            )
        }
    }
}