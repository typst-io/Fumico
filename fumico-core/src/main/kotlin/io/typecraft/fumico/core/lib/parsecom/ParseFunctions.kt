package io.typecraft.fumico.core.lib.parsecom

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.getOrHandle

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

fun <T> many(f: ParseFunction<T>): ParseFunction<List<T>> = { input ->
    val res = f(input)

    res.map { (value, input) ->
        val (tail, @Suppress("NAME_SHADOWING") input) = many(f)(input).getOrElse { Pair(emptyList(), input) }
        ok(listOf(value) + tail, input)
    }.getOrElse {
        ok(emptyList(), input)
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

fun takeWhile(cond: (Char) -> Boolean): ParseFunction<CharSequence> = body@{ input ->
    val (c, @Suppress("NAME_SHADOWING") input) = takeIf(cond)(input).getOrHandle { return@body ok("", input) }

    when (val result = takeWhile(cond)(input)) {
        is Either.Left -> ok(c.toString(), input)
        is Either.Right -> result.map { (s, input) -> Pair(c.toString() + s, input) }
    }
}

fun takeWhile1(cond: (Char) -> Boolean): ParseFunction<CharSequence> = body@{ input ->
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