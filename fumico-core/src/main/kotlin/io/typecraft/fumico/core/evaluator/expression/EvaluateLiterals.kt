package io.typecraft.fumico.core.evaluator.expression

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.FumicoEvaluationContext
import java.math.BigDecimal
import java.math.BigInteger

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.IntegerLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.Integer(BigInteger(node.token.actual)))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.DecimalLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.Decimal(BigDecimal(node.token.actual)))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal.StringLiteral): FumicoEvaluated =
    Pair(this, FumicoValue.String(node.token))

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Literal): FumicoEvaluated = when (node) {
    is Ast.Child.Expression.Literal.IntegerLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.DecimalLiteral -> evaluate(node)
    is Ast.Child.Expression.Literal.StringLiteral -> evaluate(node)
}
