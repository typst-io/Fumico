package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseError
import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseResult


fun <Meta, Token> tag(tag: List<Token>): ParseFunction<Meta, Token, List<Token>> = { input ->
    if (input.size < tag.size) {
        ParseError.eof()
    } else {
        val (left, right) = input.splitAt(tag.size)

        if (left.asSequence().zip(tag.asSequence()).all { (l, r) -> l == r }) {
            ParseResult.Ok(tag, right)
        } else {
            ParseError.tag()
        }
    }
}

fun <Meta, Token> tag(vararg tag: Token): ParseFunction<Meta, Token, List<Token>> = tag(tag.toList())