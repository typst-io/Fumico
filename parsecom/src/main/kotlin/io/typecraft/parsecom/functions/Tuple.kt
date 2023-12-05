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
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
): ParseFunction<Meta, Token, Triple<A, B, C>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }

    ParseResult.Ok(Triple(a, b, c), input3)
}

data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

inline fun <Meta, Token, A, B, C, D> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
): ParseFunction<Meta, Token, Tuple4<A, B, C, D>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple4(a, b, c, d), input4)
}

data class Tuple5<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)

inline fun <Meta, Token, A, B, C, D, E> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
): ParseFunction<Meta, Token, Tuple5<A, B, C, D, E>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple5(a, b, c, d, e), input5)
}

data class Tuple6<A, B, C, D, E, F>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F
)

inline fun <Meta, Token, A, B, C, D, E, F> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
    crossinline f5: ParseFunction<Meta, Token, F>,
): ParseFunction<Meta, Token, Tuple6<A, B, C, D, E, F>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }
    val (f, input6) = f5(input5).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple6(a, b, c, d, e, f), input6)
}

data class Tuple7<A, B, C, D, E, F, G>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G
)

inline fun <Meta, Token, A, B, C, D, E, F, G> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
    crossinline f5: ParseFunction<Meta, Token, F>,
    crossinline f6: ParseFunction<Meta, Token, G>,
): ParseFunction<Meta, Token, Tuple7<A, B, C, D, E, F, G>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }
    val (f, input6) = f5(input5).unwrapOr { return@body it.into() }
    val (g, input7) = f6(input6).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple7(a, b, c, d, e, f, g), input7)
}

data class Tuple8<A, B, C, D, E, F, G, H>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
    val eighth: H,
)

inline fun <Meta, Token, A, B, C, D, E, F, G, H> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
    crossinline f5: ParseFunction<Meta, Token, F>,
    crossinline f6: ParseFunction<Meta, Token, G>,
    crossinline f7: ParseFunction<Meta, Token, H>,
): ParseFunction<Meta, Token, Tuple8<A, B, C, D, E, F, G, H>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }
    val (f, input6) = f5(input5).unwrapOr { return@body it.into() }
    val (g, input7) = f6(input6).unwrapOr { return@body it.into() }
    val (h, input8) = f7(input7).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple8(a, b, c, d, e, f, g, h), input8)
}

data class Tuple9<A, B, C, D, E, F, G, H, I>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
    val eighth: H,
    val ninth: I,
)

inline fun <Meta, Token, A, B, C, D, E, F, G, H, I> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
    crossinline f5: ParseFunction<Meta, Token, F>,
    crossinline f6: ParseFunction<Meta, Token, G>,
    crossinline f7: ParseFunction<Meta, Token, H>,
    crossinline f8: ParseFunction<Meta, Token, I>,
): ParseFunction<Meta, Token, Tuple9<A, B, C, D, E, F, G, H, I>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }
    val (f, input6) = f5(input5).unwrapOr { return@body it.into() }
    val (g, input7) = f6(input6).unwrapOr { return@body it.into() }
    val (h, input8) = f7(input7).unwrapOr { return@body it.into() }
    val (i, input9) = f8(input8).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple9(a, b, c, d, e, f, g, h, i), input9)
}

data class Tuple10<A, B, C, D, E, F, G, H, I, J>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
    val sixth: F,
    val seventh: G,
    val eighth: H,
    val ninth: I,
    val tenth: J,
)

inline fun <Meta, Token, A, B, C, D, E, F, G, H, I, J> tuple(
    crossinline f0: ParseFunction<Meta, Token, A>,
    crossinline f1: ParseFunction<Meta, Token, B>,
    crossinline f2: ParseFunction<Meta, Token, C>,
    crossinline f3: ParseFunction<Meta, Token, D>,
    crossinline f4: ParseFunction<Meta, Token, E>,
    crossinline f5: ParseFunction<Meta, Token, F>,
    crossinline f6: ParseFunction<Meta, Token, G>,
    crossinline f7: ParseFunction<Meta, Token, H>,
    crossinline f8: ParseFunction<Meta, Token, I>,
    crossinline f9: ParseFunction<Meta, Token, J>,
): ParseFunction<Meta, Token, Tuple10<A, B, C, D, E, F, G, H, I, J>> = body@{ input ->
    val (a, input1) = f0(input).unwrapOr { return@body it.into() }
    val (b, input2) = f1(input1).unwrapOr { return@body it.into() }
    val (c, input3) = f2(input2).unwrapOr { return@body it.into() }
    val (d, input4) = f3(input3).unwrapOr { return@body it.into() }
    val (e, input5) = f4(input4).unwrapOr { return@body it.into() }
    val (f, input6) = f5(input5).unwrapOr { return@body it.into() }
    val (g, input7) = f6(input6).unwrapOr { return@body it.into() }
    val (h, input8) = f7(input7).unwrapOr { return@body it.into() }
    val (i, input9) = f8(input8).unwrapOr { return@body it.into() }
    val (j, input10) = f9(input9).unwrapOr { return@body it.into() }

    ParseResult.Ok(Tuple10(a, b, c, d, e, f, g, h, i, j), input10)
}