package io.typecraft.fumico.core

import java.math.BigDecimal
import java.math.BigInteger

sealed class FumicoValue(
    val type: FumicoType
) {

    data class Integer(val value: BigInteger) : FumicoValue(FumicoType.Integer)
    data class Decimal(val value: BigDecimal) : FumicoValue(FumicoType.Decimal)
    data class String(val value: kotlin.String) : FumicoValue(FumicoType.String)

    object Unit : FumicoValue(FumicoType.Unit)
}