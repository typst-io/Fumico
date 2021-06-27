package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction


inline fun <Meta, Input, Value> preceded(
    crossinline head: ParseFunction<Meta, Input, Any?>,
    crossinline tail: ParseFunction<Meta, Input, Value>
): ParseFunction<Meta, Input, Value> =
    mapResult(
        tuple(head, tail)
    ) { it.second }