package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseError
import io.typecraft.parsecom.ParseFunction


fun <Meta, Token, Value> filter(
    f: ParseFunction<Meta, Token, Value>,
    filter: (Value) -> Boolean
): ParseFunction<Meta, Token, Value> = { input ->
    f(input).flatMap {
        if (filter(it.value)) {
            it
        } else {
            ParseError.filter()
        }
    }
}