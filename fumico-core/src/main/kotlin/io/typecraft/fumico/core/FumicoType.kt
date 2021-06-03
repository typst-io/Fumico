package io.typecraft.fumico.core

sealed class FumicoType {
    object Integer : FumicoType()
    object Decimal : FumicoType()
    object String : FumicoType()

    data class Sum(
        val name: kotlin.String,
        val variants: Map<kotlin.String, FumicoType>
    ) : FumicoType()

    data class Product(
        val name: kotlin.String,
        val table: Map<kotlin.String, FumicoType>
    ) : FumicoType()

    companion object {
        val Unit = Product("()", emptyMap())
    }
}
