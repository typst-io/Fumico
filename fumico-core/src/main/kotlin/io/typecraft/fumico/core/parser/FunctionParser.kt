package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.ParseResult
import io.typecraft.parsecom.functions.*

val parseFunctionDefinition: FumicoParseFunction<Ast.Child.Definition.FunctionDefinition> = body@{ input ->
    val (name, input1) = parseIdentifier(input).unwrapOr { return@body it.into() }

    ParseResult.Ok(Ast.Child.Definition.FunctionDefinition(name), input1)
}

val parseFunctionDeclaration: FumicoParseFunction<Ast.Child.Statement.FunctionDeclaration> by lazy {
    mapResult(
        tuple(
            parseIdentifier,
            defaulting(
                many(preceded(skipHorizontalSpaces, parseIdentifier)),
                emptyList()
            ),
            parseFunctionTail,
        )
    ) { (name, arguments, body) ->
        Ast.Child.Statement.FunctionDeclaration(name, arguments, body)
    }
}

val parsePrefixOperatorFunctionDeclaration: FumicoParseFunction<Ast.Child.Statement.FunctionDeclaration> by lazy {
    mapResult(
        tuple(
            parsePrefixOperatorIdentifier,
            preceded(skipHorizontalSpaces, parseIdentifier),
            parseFunctionTail,
        )
    ) { (name, argument, body) ->
        Ast.Child.Statement.FunctionDeclaration(name, listOf(argument), body)
    }
}

val parseInfixOperatorFunctionDeclaration: FumicoParseFunction<Ast.Child.Statement.FunctionDeclaration> by lazy {
    mapResult(
        tuple(
            parseInfixOperatorIdentifier,
            preceded(skipHorizontalSpaces, separatedPair(parseIdentifier, skipHorizontalSpaces, parseIdentifier)),
            parseFunctionTail,
        )
    ) { (name, arguments, body) ->
        Ast.Child.Statement.FunctionDeclaration(name, arguments.toList(), body)
    }
}

val parsePostfixOperatorFunctionDeclaration: FumicoParseFunction<Ast.Child.Statement.FunctionDeclaration> by lazy {
    mapResult(
        tuple(
            parsePostfixOperatorIdentifier,
            preceded(skipHorizontalSpaces, parseIdentifier),
            parseFunctionTail,
        )
    ) { (name, argument, body) ->
        Ast.Child.Statement.FunctionDeclaration(name, listOf(argument), body)
    }
}

val parseFunctionTail: FumicoParseFunction<Ast.Child.Expression> by lazy {
    mapResult(
        tuple(
            skipHorizontalSpaces,
            token(Token.Kind.PunctuationEqualsSign),
            skipHorizontalSpaces,
            ::parseExpression,
        )
    ) {
        it.fourth
    }
}