package com.calculator.evaluation

import com.calculator.entities.EvaluationToken
import kotlinx.coroutines.Deferred


interface Evaluator {
    fun evaluateAsync(tokens: List<EvaluationToken>): Deferred<Result>
    
    interface Result {
        interface Success : Result {
            val res: String
        }

        interface Error : Result {
            val error: List<String>
        }
    }
}