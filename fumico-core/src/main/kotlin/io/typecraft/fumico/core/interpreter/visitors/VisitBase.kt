package io.typecraft.fumico.core.interpreter.visitors

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.interpreter.Context

fun Context.evaluate(node: Ast.Root): List<FumicoValue> =
    node.children.map { evaluate(it) }

fun Context.evaluate(node: Ast.Expression): FumicoValue = when (node) {
    is Ast.Expression.Literal -> evaluate(node)
}