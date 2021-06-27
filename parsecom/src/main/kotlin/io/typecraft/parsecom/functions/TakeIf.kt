package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseError
import io.typecraft.parsecom.ParseFunction

fun <Meta, Token> takeIf(cond: (Token) -> Boolean): ParseFunction<Meta, Token, Token> = { input ->
    take1<Meta, Token>()(input).flatMap {
        if (cond(it.value)) {
            it
        } else {
            ParseError.takeIf()
        }
    }
}

fun <Meta, Token> takeUnless(cond: (Token) -> Boolean): ParseFunction<Meta, Token, Token> =
    takeIf { !cond(it) }