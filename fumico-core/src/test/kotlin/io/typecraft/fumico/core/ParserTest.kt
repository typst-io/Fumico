package io.typecraft.fumico.core

import arrow.core.Either
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.ParseInput
import io.typecraft.fumico.core.parser.parseRoot
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun `it should be parsed`() {
        assertEquals(
            Either.Right(Ast.Root(emptyList())),
            parseRoot(ParseInput("")).map { it.first },
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Expression.Literal.IntegerLiteral(BigInteger("10")),
                        Ast.Expression.Literal.IntegerLiteral(BigInteger("20")),
                        Ast.Expression.Literal.IntegerLiteral(BigInteger("30")),
                        Ast.Expression.Literal.IntegerLiteral(BigInteger("40")),
                    )
                )
            ),
            parseRoot(ParseInput("""
                10
                20
                30
                40
            """)).map { it.first },
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Expression.Literal.DecimalLiteral(BigDecimal("10.0")),
                        Ast.Expression.Literal.DecimalLiteral(BigDecimal("1e3")),
                        Ast.Expression.Literal.DecimalLiteral(BigDecimal("1e-8")),
                        Ast.Expression.Literal.DecimalLiteral(BigDecimal("3.141592")),
                    )
                )
            ),
            parseRoot(ParseInput("""
                10.0
                1e3
                1e-8
                3.141592
            """)).map { it.first },
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Expression.Literal.StringLiteral("Test"),
                        Ast.Expression.Literal.StringLiteral("Escape \\ \" \n \r \t"),
                    )
                )
            ),
            parseRoot(ParseInput("""
                "Test"
                "Escape \\ \" \n \r \t"
            """.trimIndent())).map { it.first },""
        )
    }
}
