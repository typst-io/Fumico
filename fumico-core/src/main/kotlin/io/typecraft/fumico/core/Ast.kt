package io.typecraft.fumico.core

import java.math.BigDecimal
import java.math.BigInteger

sealed class Ast {
    data class Root(val children: List<Child>) : Ast()

    sealed class Child : Ast() {
        sealed class Definition : Child() {
            data class FunctionDefinition(
                val name: String,
            ) : Definition()
        }

        sealed class Statement : Child() {
            data class FunctionDeclaration(
                val name: String,
                val arguments: List<String>,
                val body: Expression,
            ) : Statement()
        }

        sealed class Expression : Child() {
            sealed class Literal : Expression() {
                data class IntegerLiteral(val value: BigInteger) : Literal()
                data class DecimalLiteral(val value: BigDecimal) : Literal()
                data class StringLiteral(val value: String, val raw: String) : Literal()
            }

            data class Name(val name: String) : Expression()

            data class FunctionCall(val function: Expression, val argument: Expression) : Expression()
        }
    }
}
