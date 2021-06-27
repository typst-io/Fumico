package io.typecraft.fumico.core

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.typecraft.fumico.core.evaluator.evaluate
import io.typecraft.fumico.core.parser.FumicoParseInput
import io.typecraft.fumico.core.parser.parseRoot
import io.typecraft.fumico.core.tokenizer.FumicoTokenizeInput
import io.typecraft.fumico.core.tokenizer.FumicoTokenizerMeta
import io.typecraft.fumico.core.tokenizer.tokenize
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

fun interpretSimple(src: String, context: FumicoEvaluationContext = FumicoEvaluationContext()): List<FumicoValue>? =
    tokenize(
        FumicoTokenizeInput(src.toList(), FumicoTokenizerMeta())
    ).flatMap {
        parseRoot(FumicoParseInput(it.value, Unit))
    }.unwrapOrNull()
        ?.value
        ?.children
        ?.fold(emptyList<FumicoEvaluated>()) { acc, curr ->
            val ctx = acc.firstOrNull()?.first ?: context
            acc + ctx.evaluate(curr)
        }
        ?.map { it.second }

class InterpreterTest {
    @Test
    fun `it should interpret number`() {
        assertEquals(
            listOf(
                FumicoValue.Integer(BigInteger("10")),
                FumicoValue.Integer(BigInteger("20")),
                FumicoValue.Integer(BigInteger("30")),
                FumicoValue.Integer(BigInteger("40"))
            ),
            interpretSimple(
                """
                    10
                    20
                    30
                    40
                """.trimIndent()
            )
        )
    }

    @Test
    fun `it should interpret function call`() {
        val inc = mockk<FumicoValue.Function>()
        every { inc.execute(any(), FumicoValue.Integer(BigInteger("1"))) } returns FumicoValue.Integer(BigInteger("2"))
        assertEquals(
            listOf(
                FumicoValue.Integer(BigInteger("2"))
            ),
            interpretSimple(
                """
                    inc 1
                """.trimIndent(),
                FumicoEvaluationContext().withBinding("inc", inc)
            )
        )
        verify { inc.execute(any(), FumicoValue.Integer(BigInteger("1"))) }

    }

    @Test
    fun `it should interpret function declaration`() {
        val addY = mockk<FumicoValue.Function>()
        val add = mockk<FumicoValue.Function>()
        every { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) } returns addY
        every { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) } returns FumicoValue.Integer(BigInteger("3"))
        assertEquals(
            FumicoValue.Integer(BigInteger("3")),
            interpretSimple(
                """
                    inc a = add 1 a
                    inc 2
                """.trimIndent(),
                FumicoEvaluationContext().withBinding("add", add)
            )?.lastOrNull()
        )
        verify { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) }
        verify { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) }
    }

    @Test
    fun `it should interpret prefix operator`() {
        assertEquals(
            FumicoValue.Integer(BigInteger("1")),
            interpretSimple(
                """
                    prefix + a = a
                    + + +1
                """.trimIndent()
            )?.lastOrNull()
        )

    }

    @Test
    fun `it should interpret postfix operator`() {
        assertEquals(
            FumicoValue.Integer(BigInteger("1")),
            interpretSimple(
                """
                    postfix !! a = a
                    1!! !! !!
                """.trimIndent()
            )?.lastOrNull()
        )
    }

    @Test
    fun `it should interpret infix operator`() {
        val addY = mockk<FumicoValue.Function>()
        val add = mockk<FumicoValue.Function>()
        every { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) } returns addY
        every { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) } returns FumicoValue.Integer(BigInteger("3"))
        assertEquals(

            FumicoValue.Integer(BigInteger("3")),
            interpretSimple(
                """
                    1 + 2
                """.trimIndent(),
                FumicoEvaluationContext().withBinding("infix +", add)
            )?.lastOrNull()
        )
        verify { add.execute(any(), FumicoValue.Integer(BigInteger("1"))) }
        verify { addY.execute(any(), FumicoValue.Integer(BigInteger("2"))) }
    }

    @Test
    fun `it should interpret tuple`() {
        assertEquals(
            FumicoValue.Tuple(
                listOf(
                    FumicoValue.Tuple(
                        listOf(
                            FumicoValue.Integer(BigInteger("1")),
                            FumicoValue.Integer(BigInteger("2")),
                            FumicoValue.Integer(BigInteger("3"))
                        )
                    ),
                    FumicoValue.Integer(BigInteger("4")),
                    FumicoValue.Unit,
                )

            ),
            interpretSimple(
                """
                    ((1, 2, 3), (4), ())
                """.trimIndent()
            )?.lastOrNull()
        )
    }
}
