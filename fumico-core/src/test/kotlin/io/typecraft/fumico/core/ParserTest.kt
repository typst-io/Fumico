package io.typecraft.fumico.core

import io.typecraft.fumico.core.parser.*
import io.typecraft.fumico.core.tokenizer.*
import kotlin.test.Test
import kotlin.test.assertEquals

fun parseSimple(src: String): List<Ast.Child>? =
    tokenize(FumicoTokenizeInput(src.toList(), meta = FumicoTokenizerMeta())).flatMap {
        parseRoot(FumicoParseInput(it.value, meta = Unit))
    }.unwrapOrNull()?.value?.children

fun integerNode(value: String): Ast.Child.Expression.Literal.IntegerLiteral =
    Ast.Child.Expression.Literal.IntegerLiteral(Token.Kind.LiteralInteger(value))

fun decimalNode(value: String): Ast.Child.Expression.Literal.DecimalLiteral =
    Ast.Child.Expression.Literal.DecimalLiteral(Token.Kind.LiteralDecimal(value))

fun decimalExponentNode(value: String): Ast.Child.Expression.Literal.DecimalLiteral =
    Ast.Child.Expression.Literal.DecimalLiteral(Token.Kind.LiteralDecimalExponent(value))

fun stringNode(value: String): Ast.Child.Expression.Literal.StringLiteral =
    Ast.Child.Expression.Literal.StringLiteral(Token.Kind.LiteralString(value))

fun identNameNode(name: String): Ast.Child.Expression.Name =
    Ast.Child.Expression.Name(Token.Kind.IdentifierIdentifier(name))

fun prefixNameNode(name: String): Ast.Child.Expression.Name =
    Ast.Child.Expression.Name(Token.Kind.IdentifierPrefix(name))

fun infixNameNode(name: String): Ast.Child.Expression.Name =
    Ast.Child.Expression.Name(Token.Kind.IdentifierInfix(name))

fun postfixNameNode(name: String): Ast.Child.Expression.Name =
    Ast.Child.Expression.Name(Token.Kind.IdentifierPostfix(name))

fun functionDeclarationNode(
    name: Token,
    arguments: List<String>,
    body: Ast.Child.Expression
): Ast.Child.Statement.FunctionDeclaration =
    Ast.Child.Statement.FunctionDeclaration(
        name,
        arguments.map { Token.Kind.IdentifierIdentifier(it) },
        body
    )

class ParserTest {
    @Test
    fun `it should parse nothing`() {
        assertEquals(
            emptyList(),
            parseSimple(""),
        )
    }

    @Test
    fun `it should parse integer`() {
        assertEquals(
            listOf(
                integerNode("10"),
                integerNode("20"),
                integerNode("30"),
                integerNode("40"),
            ),
            parseSimple(
                """
                    10
                    20
                    30
                    40
                """.trimIndent()
            ),
        )
    }

    @Test
    fun `it should parse decimal`() {
        assertEquals(
            listOf(
                decimalNode("10.0"),
                decimalExponentNode("1.0e3"),
                decimalExponentNode("1e-8"),
                decimalNode("3.141592"),
            ),
            parseSimple(
                """
                    10.0
                    1.0e3
                    1e-8
                    3.141592
                """.trimIndent()
            ),
        )
    }

    @Test
    fun `it should parse string`() {
        assertEquals(
            listOf(
                stringNode(""""Test""""),
                stringNode(""""Escape \\ \" \n \r \t""""),
            ),
            parseSimple(
                """
                    "Test"
                    "Escape \\ \" \n \r \t"
                """.trimIndent()
            )
        )
    }

    @Test
    fun `it should parse function declaration`() {
        assertEquals(
            listOf(
                functionDeclarationNode(
                    Token.Kind.IdentifierIdentifier("PI"),
                    emptyList(),
                    decimalNode("3.141592")
                ),
                functionDeclarationNode(
                    Token.Kind.IdentifierIdentifier("test"),
                    listOf("a", "b"),
                    integerNode("42")
                ),
            ),
            parseSimple(
                """
                    PI = 3.141592
                    test a b = 42
                """.trimIndent()
            )
        )

    }

    @Test
    fun `it should parse operator declaration`() {
        assertEquals(
            listOf(
                functionDeclarationNode(
                    Token.Kind.IdentifierPrefix("prefix ++"),
                    listOf("a"),
                    identNameNode("a")
                ),
                functionDeclarationNode(
                    Token.Kind.IdentifierInfix("infix &&"),
                    listOf("a", "b"),
                    identNameNode("a")
                ),
                functionDeclarationNode(
                    Token.Kind.IdentifierPostfix("postfix !!"),
                    listOf("a"),
                    identNameNode("a")
                ),
            ),
            parseSimple(
                """
                    prefix ++ a = a
                    infix && a b = a
                    postfix !! a = a
                """.trimIndent()
            )
        )

    }

    @Test
    fun `it should parse prefix operator`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.FunctionCall(
                    prefixNameNode("prefix ++"),
                    identNameNode("a"),
                ),
            ),
            parseSimple(
                """
                    ++a
                """.trimIndent()
            )
        )
    }


    @Test
    fun `it should parse postfix operator`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.FunctionCall(
                    postfixNameNode("postfix !!"),
                    identNameNode("x"),
                ),
            ),
            parseSimple(
                """
                    x!!
                """.trimIndent()
            )
        )

    }

    @Test
    fun `it should parse infix operator`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.FunctionCall(
                    Ast.Child.Expression.FunctionCall(
                        infixNameNode("infix +"),
                        identNameNode("a"),
                    ),
                    identNameNode("b"),
                ),
            ),
            parseSimple(
                """
                    a + b
                """.trimIndent()
            )
        )
    }

    @Test
    fun `it should parse right associative infix operator`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.FunctionCall(
                    Ast.Child.Expression.FunctionCall(
                        infixNameNode("infix $"),
                        identNameNode("println"),
                    ),
                    Ast.Child.Expression.FunctionCall(
                        Ast.Child.Expression.FunctionCall(
                            infixNameNode("infix +"),
                            identNameNode("a"),
                        ),
                        identNameNode("b"),
                    ),
                ),
            ),
            parseSimple(
                """
                    println $ a + b
                """.trimIndent()
            )
        )
    }

    @Test
    fun `it should parse tuple, group, or unit`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.Tuple(
                    listOf(
                        integerNode("1"),
                        integerNode("3"),
                        Ast.Child.Expression.Tuple(
                            emptyList()
                        )
                    )
                ),
            ),
            parseSimple(
                """
                    ((1), 3, ())
                """.trimIndent()
            )
        )
    }

    @Test
    fun `it should parse lambda`() {
        assertEquals(
            listOf(
                Ast.Child.Expression.Lambda(
                    emptyList(),
                    integerNode("1")
                ),
                Ast.Child.Expression.Lambda(
                    listOf(Token.Kind.IdentifierIdentifier("x")),
                    identNameNode("x")
                ),
                Ast.Child.Expression.Lambda(
                    listOf(Token.Kind.IdentifierIdentifier("x"), Token.Kind.IdentifierIdentifier("y")),
                    Ast.Child.Expression.FunctionCall(
                        Ast.Child.Expression.FunctionCall(
                            infixNameNode("infix +"),
                            identNameNode("x")
                        ),
                        identNameNode("y")
                    )
                ),
            ),
            parseSimple(
                """
                    -> 1
                    \x -> x
                    \x y -> x + y
                """.trimIndent()
            )
        )
    }
}
