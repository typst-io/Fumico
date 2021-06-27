package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseError
import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseResult


fun <Meta, Token> take1(): ParseFunction<Meta, Token, Token> = { input ->
    if (input.size < 1) {
        ParseError.eof()
    } else {
        val (left, right) = input.splitAt(1)

        ParseResult.Ok(left.asSequence().first(), right)
    }
}
