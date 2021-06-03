package io.typecraft.fumico.core

import arrow.core.Either
import io.typecraft.fumico.core.evaluator.evaluate
import io.typecraft.fumico.core.lib.parsecom.ParseInput
import io.typecraft.fumico.core.parser.parseRoot
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class InterpreterTest {
    @Test
    fun `it should run`() {
        assertEquals(
            Either.Right(
                listOf(
                    FumicoValue.Integer(BigInteger("10")),
                    FumicoValue.Integer(BigInteger("20")),
                    FumicoValue.Integer(BigInteger("30")),
                    FumicoValue.Integer(BigInteger("40"))
                )
            ),
            parseRoot(ParseInput("10 20 30 40")).map { FumicoEvaluationContext().evaluate(it.first) }
        )
    }
}
