package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult

fun <Meta, Token, Value> concat(vararg functions: ParseFunction<Meta, Token, Value>): ParseFunction<Meta, Token, List<Value>> {
    tailrec fun handle(
        index: Int,
        acc: List<Value>,
        input: ParseInput<Meta, Token>
    ): ParseResult<Meta, Token, List<Value>> {
        val f = functions.getOrNull(index) ?: return ParseResult.Ok(acc, input)

        val (res, input1) = f(input).unwrapOr { return it.into() }

        return handle(index + 1, acc + res, input1)
    }

    return { input -> handle(0, emptyList(), input) }
}


fun <Meta, Token, Value> concatWithFlatten(vararg functions: ParseFunction<Meta, Token, List<Value>>): ParseFunction<Meta, Token, List<Value>> {
    tailrec fun handle(
        index: Int,
        acc: List<Value>,
        input: ParseInput<Meta, Token>
    ): ParseResult<Meta, Token, List<Value>> {
        val f = functions.getOrNull(index) ?: return ParseResult.Ok(acc, input)

        val (res, input1) = f(input).unwrapOr { return it.into() }

        return handle(index + 1, acc + res, input1)
    }

    return { input -> handle(0, emptyList(), input) }
}
