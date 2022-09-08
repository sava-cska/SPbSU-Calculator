package com.calculator.network

import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.GetEvaluationsRequest
import com.calculator.network.request.Token
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.GetEvaluationsResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.network.response.error.GetEvaluationsErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Response

class MockApi : Api {
    override suspend fun evaluate(request: EvaluationRequest): NetworkResponse<EvaluationResult, EvaluationErrorResponse> {
        return NetworkResponse.Success(EvaluationResult("42"), Response.success(200, "OK"))
    }

    override suspend fun getEvaluations(request: GetEvaluationsRequest): NetworkResponse<GetEvaluationsResult, GetEvaluationsErrorResponse> {
        return NetworkResponse.Success(
            GetEvaluationsResult(
                listOf(EvaluationRequest(listOf(Token("Number", "123")), "3232"))
            ), Response.success(200, "OK")
        )
    }
}
