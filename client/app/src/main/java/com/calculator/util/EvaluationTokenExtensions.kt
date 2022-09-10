package com.calculator.util

import com.calculator.entities.EvaluationToken
import com.calculator.entities.Numeric
import com.calculator.entities.Operation
import com.calculator.entities.Parentheses
import com.calculator.network.request.Token

fun EvaluationToken.toJsonToken(): Token {
    return when (this) {
        is Numeric -> Token(Token.NUMBER_TYPE, value)
        is Operation.Addition -> Token(Token.OPERATION_TYPE, "+")
        is Operation.Subtract -> Token(Token.OPERATION_TYPE, "-")
        is Operation.Multiplication -> Token(Token.OPERATION_TYPE, "*")
        is Operation.Division -> Token(Token.OPERATION_TYPE, "/")
        is Parentheses.Forward -> Token(Token.PARENTHESIS_TYPE, "(")
        is Parentheses.Back -> Token(Token.PARENTHESIS_TYPE, ")")
    }
}

fun Token.toModelToken(): EvaluationToken {
    return when {
        type == Token.NUMBER_TYPE -> Numeric(body, Any())
        type == Token.OPERATION_TYPE && body == "+" -> Operation.Addition()
        type == Token.OPERATION_TYPE && body == "-" -> Operation.Subtract()
        type == Token.OPERATION_TYPE && body == "*" -> Operation.Multiplication()
        type == Token.OPERATION_TYPE && body == "/" -> Operation.Division()
        type == Token.PARENTHESIS_TYPE && body == "(" -> Parentheses.Forward()
        type == Token.PARENTHESIS_TYPE && body == ")" -> Parentheses.Back()
        else -> throw RuntimeException()
    }
}
