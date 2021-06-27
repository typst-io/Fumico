package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction

fun <Meta, Token> takeWhile(cond: (Token) -> Boolean): ParseFunction<Meta, Token, List<Token>> =
    many(takeIf(cond))

fun <Meta, Token> takeWhile1(cond: (Token) -> Boolean): ParseFunction<Meta, Token, List<Token>> =
    concatWithFlatten(mapResult(takeIf(cond)) { listOf(it) }, takeWhile(cond))