package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction

inline fun <Meta, Token, Value> separatedList(
    crossinline f: ParseFunction<Meta, Token, Value>,
    crossinline separator: ParseFunction<Meta, Token, Any?>
): ParseFunction<Meta, Token, List<Value>> =
    concatWithFlatten(mapResult(f) { listOf(it) }, many(preceded(separator, f)))
