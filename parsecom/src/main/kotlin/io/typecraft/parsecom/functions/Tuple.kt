package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseResult

inline fun <Meta, Token, A, B> tuple(
    crossinline f: ParseFunction<Meta, Token, A>,
    crossinline g: ParseFunction<Meta, Token, B>,
): ParseFunction<Meta, Token, Pair<A, B>> = body@{ input ->
    val (a, input1) = f(input).unwrapOr { return@body it.into() }
    val (b, input2) = g(input1).unwrapOr { return@body it.into() }

    ParseResult.Ok(Pair(a, b), input2)
}

inline fun <Meta, Token, A, B, C> tuple(
    crossinline f: ParseFunction<Meta, Token, A>,
    crossinline g: ParseFunction<Meta, Token, B>,
    crossinline h: ParseFunction<Meta, Token, C>,
): ParseFunction<Meta, Token, Triple<A, B, C>> = body@{ input ->
    val (a, input1) = f(input).unwrapOr { return@body it.into() }
    val (b, input2) = g(input1).unwrapOr { return@body it.into() }
    val (c, input3) = h(input2).unwrapOr { return@body it.into() }

    ParseResult.Ok(Triple(a, b, c), input3)
}

data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

inline fun <Meta, Token, A, B, C, D> tuple(
    crossinline f: ParseFunction<Meta, Token, A>,
    crossinline g: ParseFunction<Meta, Token, B>,
    crossinline h: ParseFunction<Meta, Token, C>,
    crossinline i: ParseFunction<Meta, Token, D>,
): ParseFunction<Meta, Token, Tuple4<A, B, C, D>> = body@{ input ->
    val (a, input1) = f(input).unwrapOr { return@body it.into() }
    val (b, input2) = g(input1).unwrapOr { return@body it.into() }
    val (c, input3) = h(input2).unwrapOr { return@body it.into() }
    val (d, input4) = i(input3).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple4(a, b, c, d), input4)
}