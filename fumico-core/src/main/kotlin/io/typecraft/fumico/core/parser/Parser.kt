package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.ParseResult
import io.typecraft.parsecom.functions.*


val parseRoot: FumicoParseFunction<Ast.Root> by lazy {
    mapResult(
        many(
            preceded(
                skipAllSpaces,
                alt(
                    parseStatement,
                    ::parseExpression,
                )
            )
        )
    ) {
        Ast.Root(it)
    }
}


val parseStatement: FumicoParseFunction<Ast.Child.Statement> by lazy {
    alt(
        parseFunctionDeclaration,
        parsePrefixOperatorFunctionDeclaration,
        parseInfixOperatorFunctionDeclaration,
        parsePostfixOperatorFunctionDeclaration,
    )
}

fun parseExpression(input: FumicoParseInput): FumicoParseResult<Ast.Child.Expression> =
    mapResult(
        tuple(
            parsePostfixOperatorExpression,
            defaulting(
                many(
                    preceded(
                        skipHorizontalSpaces,
                        parsePostfixOperatorExpression,
                    )
                ),
                emptyList()
            )
        )
    ) { (expression, arguments) ->
        (listOf(expression) + arguments).reduce { acc, right ->
            Ast.Child.Expression.FunctionCall(
                acc,
                right
            )
        }
    }(input)

val parseBasicExpression: FumicoParseFunction<Ast.Child.Expression> by lazy {
    alt(
        parseLiteral,
        parseName,
        parseGroupTuple,
        parseLambda,
    )
}