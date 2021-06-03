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
    val (_, input4) = opt(skipHorizontalSpaces)(input3)
    val (_, input5) = tag("=")(input4).getOrHandle { return@body err(it) }
    val (_, input6) = opt(skipHorizontalSpaces)(input5)
    val (body, input7) = parseExpression(input6).getOrHandle { return@body err(it) }

    ok(Ast.Child.Statement.FunctionDeclaration(name, arguments, body), input7)
}