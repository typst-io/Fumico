package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult
import java.lang.Exception

class ParseFailure(message: String) : Exception(message)

inline fun <Meta, Token, Value> failure(
    crossinline f: ParseFunction<Meta, Token, Value>,
    crossinline message: (ParseInput<Meta, Token>) -> String
): (ParseInput<Meta, Token>) -> ParseResult.Ok<Meta, Token, Value> = body@{ input ->
    f(input).unwrapOr { throw ParseFailure(message(input)) }
}