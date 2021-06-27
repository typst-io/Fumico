package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult


inline fun <Meta, Token, Input, Output> mapResult(
    crossinline f: ParseFunction<Meta, Token, Input>,
    crossinline mapper: ParseInput<Meta, Token>.(Input) -> Output
): ParseFunction<Meta, Token, Output> = { input ->
    f(input).flatMap { (value, input) ->
        ParseResult.Ok(input.mapper(value), input)
    }
}
