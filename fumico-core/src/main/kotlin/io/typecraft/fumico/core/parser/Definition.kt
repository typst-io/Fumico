package io.typecraft.fumico.core.parser

import io.typecraft.fumico.core.tokenizer.Token
import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseInput
import io.typecraft.parsecom.ParseResult
import io.typecraft.parsecom.functions.takeIf

typealias FumicoParseInput = ParseInput<Unit, Token>
typealias FumicoParseFunction<T> = ParseFunction<Unit, Token, T>
typealias FumicoParseResult<T> = ParseResult<Unit, Token, T>

fun token(kind: Token.Kind): FumicoParseFunction<Token> = takeIf { it.kind == kind }
