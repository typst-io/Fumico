package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.FumicoEvaluationContext

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.IntegerLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.Integer(node.value))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.DecimalLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.Decimal(node.value))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.StringLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.String(node.value))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal): FumicoEvaluated = when (node) {
    is Ast.Child.Expression.Literal.IntegerLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.DecimalLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.StringLiteral -> evaluate(node)
}
