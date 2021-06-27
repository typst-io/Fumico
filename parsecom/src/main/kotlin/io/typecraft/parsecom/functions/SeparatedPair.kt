package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction


fun <Meta, Input, A, B> separatedPair(
    head: ParseFunction<Meta, Input, A>,
    separator: ParseFunction<Meta, Input, Any?>,
    tail: ParseFunction<Meta, Input, B>,
): ParseFunction<Meta, Input, Pair<A, B>> =
    mapResult(
        tuple(head, separator, tail)
    ) { Pair(it.first, it.third) }