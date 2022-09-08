package com.calculator.util

import com.calculator.entities.EvaluationToken
import com.calculator.entities.Numeric
import com.calculator.entities.Operation
import com.calculator.entities.Parentheses
import com.calculator.network.request.Token

fun EvaluationToken.toJsonToken() : Token {
    return when(this) {
        is Numeric -> Token(Token.NUMBER_TYPE, value)
        Operation.Addition -> Token(Token.OPERATION_TYPE, "+")
        Operation.Subtract -> Token(Token.OPERATION_TYPE, "-")
        Operation.Multiplication -> Token(Token.OPERATION_TYPE, "*")
        Operation.Division -> Token(Token.OPERATION_TYPE, "/")
        Parentheses.Forward -> Token(Token.PARENTHESIS_TYPE, "(")
        Parentheses.Back -> Token(Token.PARENTHESIS_TYPE, ")")
    }
}
