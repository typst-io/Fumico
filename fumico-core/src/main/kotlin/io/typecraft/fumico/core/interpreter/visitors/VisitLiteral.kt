package io.typecraft.fumico.core.interpreter.visitors

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.interpreter.Context

fun Context.evaluate(node: Ast.Expression.Literal.IntegerLiteral): FumicoValue.Integer =
    FumicoValue.Integer(node.value)

fun Context.evaluate(node: Ast.Expression.Literal.DecimalLiteral): FumicoValue.Decimal =
    FumicoValue.Decimal(node.value)

fun Context.evaluate(node: Ast.Expression.Literal.StringLiteral): FumicoValue.String =
    FumicoValue.String(node.value)

fun Context.evaluate(node: Ast.Expression.Literal): FumicoValue = when (node) {
    is Ast.Expression.Literal.IntegerLiteral -> evaluate(node)
    is Ast.Expression.Literal.DecimalLiteral -> evaluate(node)
    is Ast.Expression.Literal.StringLiteral -> evaluate(node)
}
