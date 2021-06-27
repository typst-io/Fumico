package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseResult

fun <Meta, Token, Value> returning(value: Value): ParseFunction<Meta, Token, Value> =
    { input -> ParseResult.Ok(value, input) }