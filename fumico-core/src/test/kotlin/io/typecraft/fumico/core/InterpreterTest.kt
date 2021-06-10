package io.typecraft.fumico.core

import arrow.core.Either
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
            parseRoot(
                ParseInput(
                    """
                10
                20
                30
                40
            """
                )
            ).map { FumicoEvaluationContext().evaluate(it.first) }
        )

        val inc = mockk<FumicoValue.Function>()
        every { inc.execute(any(), FumicoValue.Integer(BigInteger("1"))) } returns FumicoValue.Integer(BigInteger("2"))
        assertEquals(
            Either.Right(
                listOf(
                    FumicoValue.Integer(BigInteger("2"))
                )
            ),
            parseRoot(
                ParseInput(
                    """
                inc 1
            """
                )
            ).map { FumicoEvaluationContext().withBinding("inc", inc).evaluate(it.first) }
        )
        verify { inc.execute(any(), FumicoValue.Integer(BigInteger("1"))) }

        val addY = mockk<FumicoValue.Function>()
        val add = mockk<FumicoValue.Function>()
        every { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) } returns addY
        every { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) } returns FumicoValue.Integer(BigInteger("3"))
        assertEquals(
            Either.Right(
                FumicoValue.Integer(BigInteger("3"))
            ),
            parseRoot(
                ParseInput(
                    """
                inc a = add 1 a
                inc 2
            """
                )
            ).map { FumicoEvaluationContext().withBinding("add", add).evaluate(it.first).last() }
        )
        verify { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) }
        verify { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) }
        assertEquals(
            Either.Right(
                FumicoValue.Integer(BigInteger("1"))
            ),
            parseRoot(
                ParseInput(
                    """
                prefix + a = a
                + + +1
            """
                )
            ).map { FumicoEvaluationContext().evaluate(it.first).last() }
        )
    }
}
