package io.typecraft.parsecom

typealias ParseFunction<Meta, Token, Value> = (ParseInput<Meta, Token>) -> ParseResult<Meta, Token, Value>