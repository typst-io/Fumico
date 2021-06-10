package io.typecraft.fumico.core.parser

import arrow.core.getOrHandle
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JOptionPane


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
        parsePrefixOperatorFunctionDeclaration,
        parseInfixOperatorFunctionDeclaration,
        parsePostfixOperatorFunctionDeclaration,
    )
}

val parseExpression: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (prefixOperators, input1) = defaulting(
        separatedList(parseSpecialIdentifier, skipHorizontalSpaces),
        emptyList()
    )(input)

    val (_, input2) = opt(skipHorizontalSpaces)(input1)

    val (expressionRaw, input3) = parseBasicExpression(input2).getOrHandle { return@body err(it) }

    val expression = prefixOperators.fold(expressionRaw) { acc, operator ->
        Ast.Child.Expression.FunctionCall(Ast.Child.Expression.Name("prefix $operator"), acc)
    }

    val (arguments, input4) = defaulting(
        many(
            preceded(
                skipHorizontalSpaces,
                parseBasicExpression,
            )
        ), emptyList()
    )(input3)

    val res = (listOf(expression).asSequence() + arguments.asSequence()).reduce { acc, right ->
        Ast.Child.Expression.FunctionCall(
            acc,
            right
        )
    }

    // TODO: infix, postfix operator

    ok(res, input4)
}

val parseBasicExpression: ParseFunction<Ast.Child.Expression> by lazy {
    alt(
        parseLiteral,
        parseName,
    )
}
