package io.typecraft.parsecom.functions

import io.typecraft.parsecom.ParseFunction
import io.typecraft.parsecom.ParseResult

inline fun <Meta, Token, Value> debug(
    crossinline f: ParseFunction<Meta, Token, Value>,
    showTraces: Boolean = false
): ParseFunction<Meta, Token, Value> =
    body@{ input ->
        System.err.println("Begin: $input")

        when (val result = f(input)) {
            is ParseResult.Ok -> {
                System.err.println("Ok: ${result.value}")
                System.err.println("End: ${result.input}")
                System.`in`.read()
                result
            }
            is ParseResult.Err -> {
                System.err.println("Err: ${result.error}")
                if (showTraces) {
                    for (trace in result.error.trace) {
                        System.err.println("    $trace")
                    }
                }
                System.`in`.read()
                result
            }
        }
    }