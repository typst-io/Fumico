package io.typecraft.fumico.core

fun makeFumicoFunction(name: String, body: () -> FumicoValue): FumicoValue.Function =
    FumicoValue.Function(name) { _, _ ->
        body()
    }

fun makeFumicoFunction(name: String, body: (FumicoValue) -> FumicoValue): FumicoValue.Function =
    FumicoValue.Function(name) { _, arg0 ->
        body(arg0)
    }

fun makeFumicoFunction(name: String, body: (FumicoValue, FumicoValue) -> FumicoValue): FumicoValue.Function =
    FumicoValue.Function(name) { _, arg0 ->
        FumicoValue.Function("${name}__lambda_1") { _, arg1 ->
            body(arg0, arg1)
        }
    }

fun makeFumicoFunction(
    name: String,
    body: (FumicoValue, FumicoValue, FumicoValue) -> FumicoValue
): FumicoValue.Function =
    FumicoValue.Function(name) { _, arg0 ->
        FumicoValue.Function("${name}__lambda_1") { _, arg1 ->
            FumicoValue.Function("${name}__lambda_2") { _, arg2 ->
                body(arg0, arg1, arg2)
            }
        }
    }
