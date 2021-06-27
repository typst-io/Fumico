package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult


fun <Meta, Token, Value> mapMeta(
    f: ParseFunction<Meta, Token, Value>,
    mapper: ParseInput<Meta, Token>.(Meta) -> Meta
): ParseFunction<Meta, Token, Value> = { input ->
    f(input).flatMap { (value, input) ->
        ParseResult.Ok(value, input.withMeta { input.mapper(it) })
    }
}
