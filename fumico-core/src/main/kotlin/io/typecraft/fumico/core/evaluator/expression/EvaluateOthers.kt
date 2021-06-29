package io.typecraft.fumico.core.evaluator.expression

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoEvaluationContext
import io.typecraft.fumico.core.FumicoValue
import io.typecraft.fumico.core.evaluator.evaluate
import java.lang.IllegalStateException

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Name): FumicoEvaluated =
    Pair(
        this,
        this.bindings[node.token.actual]
            ?: throw IllegalStateException("Cannot get a value named `${node.token.actual}`. Is it a internal compiler error?")
    )

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Tuple): FumicoEvaluated =
    if (node.values.isEmpty()) {
        Pair(
            this,
            FumicoValue.Unit
        )
    } else {
        val (context1, evaluated) = node.values.fold(Pair(this, emptyList<FumicoValue>())) { (context, acc), curr ->
            val (context1, evaluated) = context.evaluate(curr)
            Pair(context1, acc + evaluated)
        }
        Pair(
            context1,
            FumicoValue.Tuple(evaluated)
        )
    }

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Lambda): FumicoEvaluated {
    val function = node.arguments.map { it.actual }.ifEmpty { listOf("_") }.foldIndexed(
        FumicoValue.Function("${'$'}unnamed_lambda_last") { context, _ ->
            val context1 = if (node.arguments.isEmpty()) {
                this
            } else {
                context
            }
            context1.evaluate(node.body).second
        }
    ) { index, acc, argumentName ->
        FumicoValue.Function("${'$'}unnamed_lambda_$argumentName") { context, argument ->
            val context1 = if (index + 1 == node.arguments.size) {
                this
            } else {
                context
            }
            acc.execute(context1.withBinding(argumentName, argument), argument)
        }
    }

    return Pair(this, function)
}