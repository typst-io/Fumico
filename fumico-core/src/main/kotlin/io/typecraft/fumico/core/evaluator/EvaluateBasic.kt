package io.typecraft.fumico.core.evaluator

import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.FumicoEvaluated
import io.typecraft.fumico.core.FumicoEvaluationContext
import java.lang.IllegalStateException

fun FumicoEvaluationContext.evaluate(node: Ast.Child.Expression.Name): FumicoEvaluated =
    Pair(this,
        this.bindings[node.name]
            ?: throw IllegalStateException("Cannot get a value named ${node.name}. Is it a internal compiler error?")
    )