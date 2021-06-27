package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseError
import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult

fun <Meta, Token, Value> alt(variants: List<ParseFunction<Meta, Token, Value>>): ParseFunction<Meta, Token, Value> {
    tailrec fun handle(index: Int, input: ParseInput<Meta, Token>): ParseResult<Meta, Token, Value> {
        val f = variants.getOrNull(index) ?: return ParseError.alt()

        return when (val result = f(input)) {
            is ParseResult.Ok -> result
            is ParseResult.Err -> handle(index + 1, input)
        }
    }

    return { input -> handle(0, input) }
}

fun <Meta, Token, Value> alt(vararg variants: ParseFunction<Meta, Token, Value>): ParseFunction<Meta, Token, Value> =
    alt(variants.toList())