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
                        Ast.Child.Expression.Literal.IntegerLiteral(BigInteger("10")),
                        Ast.Child.Expression.Literal.IntegerLiteral(BigInteger("20")),
                        Ast.Child.Expression.Literal.IntegerLiteral(BigInteger("30")),
                        Ast.Child.Expression.Literal.IntegerLiteral(BigInteger("40")),
                    )
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
            ).map { it.first },
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal("10.0")),
                        Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal("1e3")),
                        Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal("1e-8")),
                        Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal("3.141592")),
                    )
                )
            ),
            parseRoot(
                ParseInput(
                    """
                        10.0
                        1e3
                        1e-8
                        3.141592
                    """
                )
            ).map { it.first },
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Child.Expression.Literal.StringLiteral("Test"),
                        Ast.Child.Expression.Literal.StringLiteral("Escape \\ \" \n \r \t"),
                    )
                )
            ),
            parseRoot(
                ParseInput(
                    """
                        "Test"
                        "Escape \\ \" \n \r \t"
                    """.trimIndent()
                )
            ).map { it.first }, ""
        )
        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Child.Statement.FunctionDeclaration(
                            "PI",
                            emptyList(),
                            Ast.Child.Expression.Literal.DecimalLiteral(BigDecimal("3.141592"))
                        ),
                        Ast.Child.Statement.FunctionDeclaration(
                            "test",
                            listOf("a", "b"),
                            Ast.Child.Expression.Literal.IntegerLiteral(BigInteger("42"))
                        ),
                    )
                )
            ),
            parseRoot(
                ParseInput(
                    """
                        PI = 3.141592
                        test a b = 42
                    """.trimIndent()
                )
            ).map { it.first }, ""
        )

        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Child.Statement.FunctionDeclaration(
                            "prefix ++",
                            listOf("a"),
                            Ast.Child.Expression.Name("a")
                        ),
                        Ast.Child.Statement.FunctionDeclaration(
                            "infix &&",
                            listOf("a", "b"),
                            Ast.Child.Expression.Name("a")
                        ),
                        Ast.Child.Statement.FunctionDeclaration(
                            "postfix !!",
                            listOf("a"),
                            Ast.Child.Expression.Name("a")
                        ),
                    )
                )
            ),
            parseRoot(
                ParseInput(
                    """
                        prefix ++ a = a
                        infix && a b = a
                        postfix !! a = a
                    """.trimIndent()
                )
            ).map { it.first }
        )

        assertEquals(
            Either.Right(
                Ast.Root(
                    listOf(
                        Ast.Child.Expression.FunctionCall(
                            Ast.Child.Expression.Name("prefix ++"),
                            Ast.Child.Expression.Name("a"),
                        ),
                    )
                )
            ),
            parseRoot(
                ParseInput(
                    """
                        ++a
                    """.trimIndent()
                )
            ).map { it.first }
        )
    }
}
