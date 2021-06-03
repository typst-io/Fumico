package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.FumicoEvaluationContext

fun FumicoEvaluationContext.evaluate(node: Ast.Root): List<FumicoValue> =
    node.children.map { evaluate(it) }

fun FumicoEvaluationContext.evaluate(node: Ast.Child): FumicoValue = when (node) {
    is Ast.Child.Expression -> evaluate(node)
    else -> TODO()
}

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression): FumicoValue = when (node) {
    is Ast.Child.Expression.Literal -> evaluate(node)
}