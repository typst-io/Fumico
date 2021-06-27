package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.parsecom.functions.alt
import io.typecraft.parsecom.functions.mapResult

val parseName by lazy {
    mapResult(
        alt(
            parseIdentifier,
            parsePrefixOperatorIdentifier,
            parseInfixOperatorIdentifier,
            parsePostfixOperatorIdentifier
        )
    ) {
        Ast.Child.Expression.Name(it)
    }
}