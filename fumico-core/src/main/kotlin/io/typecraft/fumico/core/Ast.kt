package io.typecraft.fumico.core

import java.math.BigDecimal
import java.math.BigInteger

sealed class Ast {
    data class Root(val children: List<Expression>) : Ast()

    sealed class Expression : Ast() {
        sealed class Literal : Expression() {
            data class IntegerLiteral(val value: BigInteger) : Literal()
            data class DecimalLiteral(val value: BigDecimal) : Literal()
            data class StringLiteral(val value: String) : Literal()
        }

        sealed class FunctionCall(val function: Expression, val argument: Expression) : Expression()
    }
}
