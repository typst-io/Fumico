package io.typecraft.parsecom

sealed class ParseError(private val name: String, val trace: Array<StackTraceElement>) {
    class Eof(trace: Array<StackTraceElement>) : ParseError("Eof", trace)
    class Tag(trace: Array<StackTraceElement>) : ParseError("Tag", trace)

    class Alt(trace: Array<StackTraceElement>) : ParseError("Alt", trace)

    class TakeIf(trace: Array<StackTraceElement>) : ParseError("TakeIf", trace)
    class TakeWhile1(trace: Array<StackTraceElement>) : ParseError("TakeWhile1", trace)

    class Filter(trace: Array<StackTraceElement>) : ParseError("Filter", trace)

    class Unknown(trace: Array<StackTraceElement>) : ParseError("Unknown", trace)

    override fun toString(): String = "ParseError.$name"

    companion object {
        fun <Meta, Token, Value> eof(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(Eof(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> tag(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(Tag(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> alt(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(Alt(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> takeIf(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(TakeIf(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> takeWhile1(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(TakeWhile1(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> filter(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(Filter(Thread.currentThread().stackTrace))

        fun <Meta, Token, Value> unknown(): ParseResult<Meta, Token, Value> =
            ParseResult.Err(Unknown(Thread.currentThread().stackTrace))
    }
}