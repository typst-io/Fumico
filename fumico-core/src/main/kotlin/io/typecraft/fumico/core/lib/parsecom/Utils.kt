package io.typecraft.fumico.core.lib.parsecom

import arrow.core.Either

typealias ParseResult<T> = Either<ParseError, Pair<T, ParseInput>>

fun <T> ok(value: T, input: ParseInput): ParseResult<T> =
    Either.Right(Pair(value, input))

fun <T> err(error: ParseError): ParseResult<T> =
    Either.Left(error)
