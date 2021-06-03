package io.typecraft.fumico.core

data class FumicoEvaluationContext(
    val parent: FumicoEvaluationContext? = null,
    val bindings: Map<String, FumicoValue> = mapOf(),
) {
    fun withVariable(name: String, value: FumicoValue): FumicoEvaluationContext =
        FumicoEvaluationContext(
            parent,
            bindings + mapOf(Pair(name, value))
        )

    fun createChild(): FumicoEvaluationContext = FumicoEvaluationContext(this)
}
