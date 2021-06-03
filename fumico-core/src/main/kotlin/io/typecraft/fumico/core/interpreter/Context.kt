package io.typecraft.fumico.core.interpreter

import io.typecraft.fumico.core.FumicoValue

data class Context(
    val parent: Context? = null,
    val bindings: Map<String, FumicoValue> = mapOf()
) {

    operator fun plus(pair: Pair<String, FumicoValue>): Context =
        Context(
            parent,
            bindings + mapOf(pair)
        )

    fun createChild(): Context = Context(this)
}
