package io.typecraft.fumico.core

sealed class FumicoType {
    object Integer : FumicoType()
    object Decimal : FumicoType()
    object String : FumicoType()

    data class Sum(
        val name: String,
        val variants: Map<String, FumicoType>
    ) : FumicoType()

    data class Product(
        val name: String,
        val table: Map<String, FumicoType>
    ) : FumicoType()
}
