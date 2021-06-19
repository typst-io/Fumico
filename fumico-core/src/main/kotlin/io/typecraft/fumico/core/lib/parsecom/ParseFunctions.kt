package io.typecraft.fumico.core.lib.parsecom

import arrow.core.*

typealias ParseFunction<T> = (ParseInput) -> ParseResult<T>

val anyChar: ParseFunction<Char> = { input ->
    if (input.size < 1) {
        err(ParseError.Eof)
    } else {
        val (left, right) = input.splitAt(1)

        ok(left.asSequence().first(), right)
    }
}

fun tag(tag: String): ParseFunction<String> = { input ->
    if (input.size < tag.length) {
        err(ParseError.Eof)
    } else {
        val (left, right) = input.splitAt(tag.length)

        if (left.asSequence().zip(tag.asSequence()).all { (l, r) -> l == r }) {
            ok(tag, right)
        } else {
            err(ParseError.Tag)
        }
    }
}


fun <T> many(f: ParseFunction<T>): ParseFunction<List<T>> {
    tailrec fun handle(input: ParseInput, acc: List<T>): ParseResult<List<T>> {
        val (res, input1) = f(input).getOrHandle { return ok(acc, input) }
        return handle(input1, acc + res)
    }
    return { input -> handle(input, emptyList()) }
}

fun <T> many1(f: ParseFunction<T>): ParseFunction<List<T>> = { input ->
    f(input).flatMap { (first, input1) -> mapResult(many(f)) { listOf(first) + it }(input1) }
}


fun <T> separatedList(f: ParseFunction<T>, sep: ParseFunction<Nothing?>): ParseFunction<List<T>> = { input ->
    val res = f(input)

    res.map { (value, input) ->
        val (list, input1) = defaulting(many(preceded(sep, f)), emptyList())(input)
        Pair(listOf(value) + list, input1)
    }
}

fun <T> alt(first: ParseFunction<T>, vararg tails: ParseFunction<T>): ParseFunction<T> = { input ->
    if (tails.isEmpty()) {
        first(input)
    } else {
        when (val result = first(input)) {
            is Either.Left -> alt(tails[0], *tails.drop(1).toTypedArray())(input)
            is Either.Right -> result
        }
    }
}

fun takeIf(cond: (Char) -> Boolean): ParseFunction<Char> = { input ->
    anyChar(input).flatMap { (c, input) ->
        if (cond(c)) {
            ok(c, input)
        } else {
            err(ParseError.TakeIf)
        }
    }
}

fun takeUnless(cond: (Char) -> Boolean): ParseFunction<Char> = takeIf { !cond(it) }

fun takeWhile(cond: (Char) -> Boolean): ParseFunction<String> = body@{ input ->
    val (c, @Suppress("NAME_SHADOWING") input) = takeIf(cond)(input).getOrHandle { return@body ok("", input) }

    when (val result = takeWhile(cond)(input)) {
        is Either.Left -> ok(c.toString(), input)
        is Either.Right -> result.map { (s, input) -> Pair(c.toString() + s, input) }
    }
}

fun takeWhile1(cond: (Char) -> Boolean): ParseFunction<String> = body@{ input ->
    val (c, @Suppress("NAME_SHADOWING") input) = takeIf(cond)(input).getOrHandle { return@body err(ParseError.TakeWhile1) }

    takeWhile(cond)(input).map { (s, input) ->
        Pair(c.toString() + s, input)
    }
}

fun <T, R> mapResult(f: ParseFunction<T>, mapper: (T) -> R): ParseFunction<R> = { input ->
    f(input).map { (value, input) ->
        Pair(mapper(value), input)
    }
}

fun <T> returning(value: T): ParseFunction<T> = { input -> ok(value, input) }

fun <T> skip(f: ParseFunction<T>): ParseFunction<Nothing?> = mapResult(f) { null }

fun <A, B> tuple(f: ParseFunction<A>, g: ParseFunction<B>): ParseFunction<Pair<A, B>> = body@{ input ->
    val (a, input1) = f(input).getOrHandle { return@body err(it) }
    val (b, input2) = g(input1).getOrHandle { return@body err(it) }

    ok(Pair(a, b), input2)
}

fun <A, B, C> tuple(f: ParseFunction<A>, g: ParseFunction<B>, h: ParseFunction<C>): ParseFunction<Triple<A, B, C>> =
    body@{ input ->
        val (a, input1) = f(input).getOrHandle { return@body err(it) }
        val (b, input2) = g(input1).getOrHandle { return@body err(it) }
        val (c, input3) = h(input2).getOrHandle { return@body err(it) }

        ok(Triple(a, b, c), input3)
    }

fun concat(f: ParseFunction<String>, vararg tails: ParseFunction<String>): ParseFunction<String> =
    mapResult(
        tuple(
            f,
            if (tails.isEmpty()) {
                returning("")
            } else {
                concat(tails[0], *tails.drop(1).toTypedArray())
            }
        ),
    ) {
        it.first + it.second
    }

fun <T> delimited(head: ParseFunction<Any?>, body: ParseFunction<T>, tail: ParseFunction<Any?>): ParseFunction<T> =
    mapResult(
        tuple(head, body, tail)
    ) { it.second }

fun <T> preceded(head: ParseFunction<Any?>, body: ParseFunction<T>): ParseFunction<T> =
    mapResult(
        tuple(head, body)
    ) { it.second }

fun <A, B> separatedPair(
    head: ParseFunction<A>,
    separator: ParseFunction<Any?>,
    tail: ParseFunction<B>
): ParseFunction<Pair<A, B>> =
    mapResult(
        tuple(head, separator, tail)
    ) { Pair(it.first, it.third) }

fun <T : Any?> opt(f: ParseFunction<T>): (ParseInput) -> Pair<T?, ParseInput> = defaulting(f, null)

fun <T : Any?> defaulting(f: ParseFunction<T>, default: T): (ParseInput) -> Pair<T, ParseInput> =
    defaulting(f) { default }

fun <T : Any?> defaulting(f: ParseFunction<T>, default: () -> T): (ParseInput) -> Pair<T, ParseInput> =
    body@{ input ->
        f(input).getOrHandle { return@body Pair(default(), input) }
    }

fun <T: Any?> okOf(f: (ParseInput) -> Pair<T, ParseInput>): ParseFunction<T> = { input ->
    Either.Right(f(input))
}

fun <T> filter(f: ParseFunction<T>, filter: (T) -> Boolean): ParseFunction<T> = { input ->
    f(input).flatMap {
        if (filter(it.first)) {
            Either.Right(it)
        } else {
            Either.Left(ParseError.Filter)
        }
    }
}