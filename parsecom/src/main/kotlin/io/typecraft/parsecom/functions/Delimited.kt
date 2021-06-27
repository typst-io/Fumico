package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction

fun <Meta, Input, Value> delimited(
    head: ParseFunction<Meta, Input, Any?>,
    body: ParseFunction<Meta, Input, Value>,
    tail: ParseFunction<Meta, Input, Any?>
): ParseFunction<Meta, Input, Value> =
    mapResult(
        tuple(head, body, tail)
    ) { it.second }