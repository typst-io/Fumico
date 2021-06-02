package io.github.fumico.core

import arrow.core.Either
import io.typecraft.fumico.core.Ast
import io.typecraft.fumico.core.lib.parsecom.ParseInput
import io.typecraft.fumico.core.parser.parseRoot
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
            Either.Right(Ast.Root(listOf(Ast.Expression.Literal.IntegerLiteral(BigInteger("10"))))),
            parseRoot(ParseInput("10")).map { it.first },
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
            parseRoot(ParseInput("10 20 30 40")).map { it.first },
        )
    }
}
