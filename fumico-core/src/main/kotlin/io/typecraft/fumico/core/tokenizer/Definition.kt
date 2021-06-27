package io.typecraft.fumico.core.tokenizer

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult

data class FumicoTokenizerMeta(
    val line: Int = 0,
    val lastOffset: Int = 0,
)

fun FumicoTokenizeInput.createToken(kind: Token.Kind, data: List<Char>, span: Span): Token =
    Token(kind, data.joinToString(""), span)

fun FumicoTokenizeInput.createToken(kind: Token.Kind, data: CharSequence, span: Span): Token =
    createToken(kind, data.toString(), span)

fun FumicoTokenizeInput.createToken(kind: Token.Kind, data: Char, span: Span): Token =
    Token(kind, data.toString(), span)

typealias FumicoTokenizeInput = ParseInput<FumicoTokenizerMeta, Char>
typealias FumicoTokenizeFunction<T> = ParseFunction<FumicoTokenizerMeta, Char, T>
typealias FumicoTokenizeResult<T> = ParseResult<FumicoTokenizerMeta, Char, T>

fun <Value> createToken(
    kind: Token.Kind,
    f: FumicoTokenizeFunction<Value>,
    mapper: (Value) -> String,
): FumicoTokenizeFunction<Token> = { input ->
    f(input).flatMap { (value, input1) ->
        val begin = Span.LineColumn(input.start, input.meta.line, input.meta.lastOffset)
        val end = Span.LineColumn(input1.start, input1.meta.line, input1.meta.lastOffset)
        ParseResult.Ok(Token(kind, mapper(value), Span(begin, end)), input1)
    }
}
