package com.calculator.network

import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.GetEvaluationsRequest
import com.calculator.network.request.Token
import com.calculator.network.response.EvaluationRequestAndResult

import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.GetEvaluationsResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.network.response.error.GetEvaluationsErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.delay

import retrofit2.Response

class MockApi : Api {
    override suspend fun evaluate(request: EvaluationRequest): NetworkResponse<EvaluationResult, EvaluationErrorResponse> {
        delay(500)
        return NetworkResponse.Success(
            EvaluationResult(kotlin.random.Random.nextInt().toString()),
            Response.success(200, "OK")
        )
    }

    override suspend fun getEvaluations(request: GetEvaluationsRequest): NetworkResponse<GetEvaluationsResult, GetEvaluationsErrorResponse> {
        delay(500)
        return NetworkResponse.Success(
            GetEvaluationsResult(
                listOf(
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                    EvaluationRequestAndResult(
                        EvaluationRequest(
                            listOf(Token(Token.NUMBER_TYPE, "42")),
                            "67"
                        ), "137"
                    ),
                )

            ), Response.success(200, "OK")
        )
    }
}
