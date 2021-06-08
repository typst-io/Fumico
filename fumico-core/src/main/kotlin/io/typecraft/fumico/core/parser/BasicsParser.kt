package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.ParseFunction
import io.typecraft.fumico.core.lib.parsecom.mapResult

val parseName: ParseFunction<Ast.Child.Expression.Name> by lazy {
    mapResult(parseIdentifier) {
        Ast.Child.Expression.Name(it)
    }
}