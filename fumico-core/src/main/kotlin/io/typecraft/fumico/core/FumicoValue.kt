package io.typecraft.fumico.core

import io.typecraft.fumico.core.tokenizer.Token
import java.math.BigDecimal
import java.math.BigInteger

sealed class FumicoValue {

    data class Integer(val value: BigInteger) : FumicoValue()
    data class Decimal(val value: BigDecimal) : FumicoValue()
    class String(val value: kotlin.String, val raw: kotlin.String) : FumicoValue() {
        companion object {
            val ShouldBeEscaped = Regex("""\\[\\]""")
        }

        constructor(token: Token) : this(
            token.actual.replace(ShouldBeEscaped) {
                when (it.value) {
                    else -> it.value
                }
            },
            token.actual
        )
    }

    data class Tuple(val values: List<FumicoValue>) : FumicoValue()

    companion object {
        val Unit = Tuple(emptyList())
    }

    data class Function(
        val name: kotlin.String,
        val body: (context: FumicoEvaluationContext, FumicoValue) -> FumicoValue
    ) : FumicoValue() {
        fun execute(context: FumicoEvaluationContext, argument: FumicoValue): FumicoValue =
            body(context, argument)
    }
}
