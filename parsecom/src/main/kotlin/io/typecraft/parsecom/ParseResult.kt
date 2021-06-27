package io.typecraft.parsecom

sealed class ParseResult<Meta, Token, out Value> {
    data class Ok<Meta, Token, out Value>(
        val value: Value,
        val input: ParseInput<Meta, Token>
    ) : ParseResult<Meta, Token, Value>()

    data class Err<Meta, Token, out Value>(val error: ParseError) : ParseResult<Meta, Token, Value>() {
        fun <NewMeta, NewToken, NewValue> into(): Err<NewMeta, NewToken, NewValue> = Err(error)
    }

    inline fun <NewMeta, NewToken, NewValue> flatMap(
        crossinline mapper: (Ok<Meta, Token, Value>) -> ParseResult<NewMeta, NewToken, NewValue>
    ): ParseResult<NewMeta, NewToken, NewValue> =
        when (this) {
            is Ok -> mapper(this)
            is Err -> this.into()
        }


    inline fun unwrapOr(body: (Err<Meta, Token, Value>) -> Nothing): Ok<Meta, Token, Value> =
        when (this) {
            is Ok -> this
            is Err -> body(this)
        }

    fun unwrapOrNull(): Ok<Meta, Token, Value>? =
        when (this) {
            is Ok -> this
            is Err -> null
        }
}

fun <Meta, Token, Value> ParseResult<Meta, Token, Value>.orElse(
    body: (ParseResult.Err<Meta, Token, Value>) -> ParseResult<Meta, Token, Value>
): ParseResult<Meta, Token, Value> =
    when (this) {
        is ParseResult.Ok -> this
        is ParseResult.Err -> body(this)
    }