package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoEvaluationContext
import io.typecraft.fumico.core.FumicoValue

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.FunctionCall): FumicoEvaluated {
    val (context1, function) = this.evaluate(node.function)
    val (context2, argument) = context1.evaluate(node.argument)

    function as FumicoValue.Function
    val result = function.execute(context2, argument)
    return Pair(context2, result)
}
