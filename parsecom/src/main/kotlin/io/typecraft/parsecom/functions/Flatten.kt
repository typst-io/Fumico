package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction

fun <Meta, Token, Value> flatten(f: ParseFunction<Meta, Token, List<List<Value>>>): ParseFunction<Meta, Token, List<Value>> =
    mapResult(f) { it.flatten() }
