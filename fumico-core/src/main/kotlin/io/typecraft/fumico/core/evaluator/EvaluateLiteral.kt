package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.FumicoEvaluationContext

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.IntegerLiteral): FumicoValue.Integer =
    FumicoValue.Integer(node.value)

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.DecimalLiteral): FumicoValue.Decimal =
    FumicoValue.Decimal(node.value)

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.StringLiteral): FumicoValue.String =
    FumicoValue.String(node.value)

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal): FumicoValue = when (node) {
    is Ast.Child.Expression.Literal.IntegerLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.DecimalLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.StringLiteral -> evaluate(node)
}
