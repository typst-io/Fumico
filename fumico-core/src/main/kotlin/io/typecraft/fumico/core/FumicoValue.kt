package io.typecraft.fumico.core

import java.math.BigDecimal
import java.math.BigInteger

sealed class FumicoValue {

    data class Integer(val value: BigInteger) : FumicoValue()
    data class Decimal(val value: BigDecimal) : FumicoValue()
    data class String(val value: kotlin.String) : FumicoValue()

    data class Function(
        val name: kotlin.String,
        val body: (context: FumicoEvaluationContext, FumicoValue) -> FumicoValue
    ) : FumicoValue() {
        fun execute(context: FumicoEvaluationContext, argument: FumicoValue): FumicoValue =
            body(context, argument)
    }

    object Unit : FumicoValue()
}