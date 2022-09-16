package com.calculator.network

import com.calculator.entities.EvaluationToken
import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.GetEvaluationsRequest
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.GetEvaluationsResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.network.response.error.GetEvaluationsErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @POST("evaluations/calculate")
    suspend fun evaluate(
        @Body request: EvaluationRequest
    ): NetworkResponse<EvaluationResult, EvaluationErrorResponse>

    @POST("evaluations/list")
    suspend fun getEvaluations(
        @Body request: GetEvaluationsRequest,
    ): NetworkResponse<GetEvaluationsResult, GetEvaluationsErrorResponse>
}
