package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.ParseFunction
import io.typecraft.fumico.core.lib.parsecom.alt
import io.typecraft.fumico.core.lib.parsecom.mapResult

val parseName: ParseFunction<Ast.Child.Expression.Name> by lazy {
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