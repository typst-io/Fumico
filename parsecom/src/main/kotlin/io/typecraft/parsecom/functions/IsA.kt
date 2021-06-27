package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction

fun <Meta, Token> isA(token: Token): ParseFunction<Meta, Token, Token> =
    takeIf { it == token }