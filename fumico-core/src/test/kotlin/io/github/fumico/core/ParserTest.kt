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
    }
}
