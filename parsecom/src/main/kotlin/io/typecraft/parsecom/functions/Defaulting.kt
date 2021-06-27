package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult

inline fun <Meta, Token, Value : Any?> defaulting(
    crossinline f: ParseFunction<Meta, Token, Value>,
    default: Value
): (ParseInput<Meta, Token>) -> ParseResult.Ok<Meta, Token, Value> = defaulting(f) { default }

inline fun <Meta, Token, Value : Any?> defaulting(
    crossinline f: ParseFunction<Meta, Token, Value>,
    crossinline default: () -> Value
): (ParseInput<Meta, Token>) -> ParseResult.Ok<Meta, Token, Value> =
    body@{ input ->
        f(input).unwrapOr { return@body ParseResult.Ok(default(), input) }
    }
