package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoEvaluationContext
import io.typecraft.fumico.core.FumicoValue

// TODO: Haskell스러운 인자 패턴 매칭을 구현해봐야 함


fun FumicoEvaluationContext.evaluate(node: Ast.Child.Statement.FunctionDeclaration): FumicoEvaluated {
    val function = node.arguments.ifEmpty { listOf("_") }.foldIndexed(
        FumicoValue.Function("${node.name}_lambda_last") { context, _ ->
            val context1 = if (node.arguments.isEmpty()) {
                this
            } else {
                context
            }
            context1.evaluate(node.body).second
        }
    ) { index, acc, argumentName ->
        FumicoValue.Function("${node.name}_lambda_$argumentName") { context, argument ->
            val context1 = if (index + 1 == node.arguments.size) {
                this
            } else {
                context
            }
            acc.execute(context1.withBinding(argumentName, argument), argument)
        }
    }

    val context1 = this.withBinding(
        node.name, function
    )

    return Pair(context1, FumicoValue.Unit)
}
