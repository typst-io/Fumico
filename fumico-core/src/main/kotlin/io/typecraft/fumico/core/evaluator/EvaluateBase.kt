package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.FumicoEvaluationContext

fun FumicoEvaluationContext.evaluate(node: Ast.Root): List<FumicoValue> =
    node.children.fold(emptyList<FumicoEvaluated>()) { acc, child ->
        acc + (acc.lastOrNull()?.first ?: this).evaluate(child)
    }.map { it.second }


fun FumicoEvaluationContext.evaluate(node: Ast.Child): FumicoEvaluated = when (node) {
    is Ast.Child.Expression -> evaluate(node)
    is Ast.Child.Definition -> {
        // definition does nothing at runtime
        Pair(this, FumicoValue.Unit)
    }
    is Ast.Child.Statement -> evaluate(node)
}

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Statement): FumicoEvaluated = when (node) {
    is Ast.Child.Statement.FunctionDeclaration -> evaluate(node)
}

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression): FumicoEvaluated = when (node) {
    is Ast.Child.Expression.Literal -> evaluate(node)
    is Ast.Child.Expression.FunctionCall -> evaluate(node)
    is Ast.Child.Expression.Name -> evaluate(node)
}