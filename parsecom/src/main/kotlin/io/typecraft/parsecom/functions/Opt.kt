package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult


inline fun <Meta, Token, Value : Any?> opt(
    crossinline f: ParseFunction<Meta, Token, Value>
): (ParseInput<Meta, Token>) -> ParseResult.Ok<Meta, Token, Value?> =
    defaulting(f) { null }