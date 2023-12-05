package io.typecraft.fumico.core

import io.typecraft.fumico.core.tokenizer.Token
import java.math.BigDecimal
import java.math.BigInteger

sealed class Ast {
    data class Root(val children: List<Child>) : Ast()

    sealed class Child : Ast() {
        sealed class Definition : Child() {
            data class FunctionDefinition(
                val name: Token,
            ) : Definition()
        }

        sealed class Statement : Child() {
            data class FunctionDeclaration(
                val name: Token,
                val arguments: List<Token>,
                val body: Expression,
            ) : Statement()
        }

        sealed class Expression : Child() {
            sealed class Literal : Expression() {
                data class IntegerLiteral(val token: Token) : Literal()
                data class DecimalLiteral(val token: Token) : Literal()
                data class StringLiteral(val token: Token) : Literal()
            }

            data class Lambda(val arguments: List<Token>, val body: Expression) : Expression()

            data class If(val cond: Expression, val then: Expression, val otherwise: Expression?) : Expression()

            data class Name(val token: Token) : Expression()

            data class FunctionCall(val function: Expression, val argument: Expression) : Expression()

            data class Tuple(val values: List<Expression>) : Expression()
        }
    }
}
