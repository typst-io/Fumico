package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult


fun <Meta, Token, Value> many(f: ParseFunction<Meta, Token, Value>): ParseFunction<Meta, Token, List<Value>> {
    tailrec fun handle(input: ParseInput<Meta, Token>, acc: List<Value>): ParseResult<Meta, Token, List<Value>> {
        val (res, input1) = f(input).unwrapOr { return ParseResult.Ok(acc, input) }
        return handle(input1, acc + res)
    }
    return { input -> handle(input, emptyList()) }
}
