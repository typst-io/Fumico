package io.typecraft.fumico.core.lib.parsecom

sealed class ParseError {
    object Eof : ParseError()
    object Tag : ParseError()

    object TakeIf : ParseError()
    object TakeWhile1 : ParseError()

    object Unknown : ParseError()
}