package io.typecraft.fumico.core

data class FumicoEvaluationContext(
    val parent: FumicoEvaluationContext? = null,
    val bindings: Map<String, FumicoValue> = mapOf(),
) {
    fun withBinding(name: String, value: FumicoValue): FumicoEvaluationContext =
        if (name == "_") {
            this
        } else {
            FumicoEvaluationContext(
                parent,
                bindings + mapOf(Pair(name, value))
            )
        }

    fun createChild(): FumicoEvaluationContext = FumicoEvaluationContext(this)
}

typealias FumicoEvaluated = Pair<FumicoEvaluationContext, FumicoValue>