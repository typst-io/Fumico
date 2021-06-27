package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoEvaluationContext
import io.typecraft.fumico.core.FumicoValue
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
