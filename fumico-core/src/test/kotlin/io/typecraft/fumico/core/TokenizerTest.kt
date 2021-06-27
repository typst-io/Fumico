package io.typecraft.fumico.core

import io.typecraft.fumico.core.tokenizer.*
import kotlin.test.Test
import kotlin.test.assertEquals


fun tokenizeSimple(src: String): List<Token>? =
    tokenize(FumicoTokenizeInput(src.toList(), meta = FumicoTokenizerMeta())).unwrapOrNull()?.value

class TokenizerTest {
    @Test
    fun `it should tokenize nothing`() {
        assertEquals(
            listOf(),
            tokenizeSimple("")
        )
    }

    @Test
    fun `it should tokenize integer`() {
        assertEquals(
            listOf(Token.Kind.LiteralInteger("10")),
            tokenizeSimple("10")
        )
        assertEquals(
            listOf(Token.Kind.LiteralInteger("20")),
            tokenizeSimple("20")
        )
    }

    @Test
    fun `it should tokenize decimal`() {
        assertEquals(
            listOf(Token.Kind.LiteralDecimal("10.0")),
            tokenizeSimple("10.0")
        )
        assertEquals(
            listOf(Token.Kind.LiteralDecimalExponent("1.0e3")),
            tokenizeSimple("1.0e3")
        )
        assertEquals(
            listOf(Token.Kind.LiteralDecimalExponent("1.1e-8")),
            tokenizeSimple("1.1e-8")
        )
        assertEquals(
            listOf(Token.Kind.LiteralDecimal("3.141592")),
            tokenizeSimple("3.141592")
        )
    }

    @Test
    fun `it should tokenize identifier`() {
        assertEquals(
            listOf(Token.Kind.IdentifierIdentifier("PI")),
            tokenizeSimple("PI")
        )
        assertEquals(
            listOf(Token.Kind.IdentifierPrefix("prefix ++")),
            tokenizeSimple("prefix ++")
        )
        assertEquals(
            listOf(Token.Kind.IdentifierInfix("infix ++")),
            tokenizeSimple("infix ++")
        )
        assertEquals(
            listOf(Token.Kind.IdentifierPostfix("postfix ++")),
            tokenizeSimple("postfix ++")
        )
    }
}
