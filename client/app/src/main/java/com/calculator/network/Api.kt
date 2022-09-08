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

interface Api {
    @POST("evaluate")
    suspend fun evaluate(
        @Body request: EvaluationRequest
    ): NetworkResponse<EvaluationResult, EvaluationErrorResponse>

    @GET("getEvaluations")
    suspend fun getEvaluations(
        @Body request: GetEvaluationsRequest
    ): NetworkResponse<GetEvaluationsResult, GetEvaluationsErrorResponse>
}
