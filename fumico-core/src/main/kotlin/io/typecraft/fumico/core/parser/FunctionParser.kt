package io.typecraft.fumico.core.parser

import arrow.core.getOrHandle
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.*

val parseFunctionDefinition: ParseFunction<Ast.Child.Definition.FunctionDefinition> = body@{ input ->
    val (name, input1) = parseIdentifier(input).getOrHandle { return@body err(it) }

    ok(Ast.Child.Definition.FunctionDefinition(name), input1)
}

val parseFunctionDeclaration: ParseFunction<Ast.Child.Statement.FunctionDeclaration> = body@{ input ->
    val (name, input1) = parseIdentifier(input).getOrHandle { return@body err(it) }
    val (_, input2) = opt(skipHorizontalSpaces)(input1)
    val (arguments, input3) = defaulting(separatedList(parseIdentifier, skipHorizontalSpaces), emptyList())(input2)
    val (body, input7) = parseFunctionTail(input3).getOrHandle { return@body err(it) }

    ok(Ast.Child.Statement.FunctionDeclaration(name, arguments, body), input7)
}

val parsePrefixOperatorFunctionDeclaration: ParseFunction<Ast.Child.Statement.FunctionDeclaration> = body@{ input ->
    val (name, input1) = parsePrefixOperatorIdentifier(input).getOrHandle { return@body err(it) }
    val (_, input2) = opt(skipHorizontalSpaces)(input1)
    val (arguments, input3) = mapResult(parseIdentifier) { listOf(it) }(input2).getOrHandle { return@body err(it) }
    val (body, input7) = parseFunctionTail(input3).getOrHandle { return@body err(it) }

    ok(Ast.Child.Statement.FunctionDeclaration(name, arguments, body), input7)
}


val parseInfixOperatorFunctionDeclaration: ParseFunction<Ast.Child.Statement.FunctionDeclaration> = body@{ input ->
    val (name, input1) = parseInfixOperatorIdentifier(input).getOrHandle { return@body err(it) }
    val (_, input2) = opt(skipHorizontalSpaces)(input1)
    val (arguments, input3) = mapResult(
        separatedPair(
            parseIdentifier,
            skipHorizontalSpaces,
            parseIdentifier
        )
    ) { it.toList() }(input2).getOrHandle { return@body err(it) }
    val (body, input7) = parseFunctionTail(input3).getOrHandle { return@body err(it) }

    ok(Ast.Child.Statement.FunctionDeclaration(name, arguments, body), input7)
}

val parsePostfixOperatorFunctionDeclaration: ParseFunction<Ast.Child.Statement.FunctionDeclaration> = body@{ input ->
    val (name, input1) = parsePostfixOperatorIdentifier(input).getOrHandle { return@body err(it) }
    val (_, input2) = opt(skipHorizontalSpaces)(input1)
    val (arguments, input3) = mapResult(parseIdentifier) { listOf(it) }(input2).getOrHandle { return@body err(it) }
    val (body, input7) = parseFunctionTail(input3).getOrHandle { return@body err(it) }

    ok(Ast.Child.Statement.FunctionDeclaration(name, arguments, body), input7)
}

val parseFunctionTail: ParseFunction<Ast.Child.Expression> = body@{ input ->
    val (_, input1) = opt(skipHorizontalSpaces)(input)
    val (_, input2) = tag("=")(input1).getOrHandle { return@body err(it) }
    val (_, input3) = opt(skipHorizontalSpaces)(input2)
    val (body, input4) = parseExpression(input3).getOrHandle { return@body err(it) }

    ok(body, input4)
}