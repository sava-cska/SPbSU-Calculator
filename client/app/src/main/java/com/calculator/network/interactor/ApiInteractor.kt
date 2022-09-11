package com.calculator.network.interactor

import com.calculator.entities.EmptyField
import com.calculator.entities.EvaluationToken
import com.calculator.entities.ListItem
import com.calculator.network.Api
import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.GetEvaluationsRequest
import com.calculator.network.request.Token
import com.calculator.network.response.EvaluationResult
import com.calculator.network.response.GetEvaluationsResult
import com.calculator.network.response.error.EvaluationErrorResponse
import com.calculator.network.response.error.GetEvaluationsErrorResponse
import com.calculator.util.toJsonToken
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiInteractor @Inject constructor(
    private val apiLazy: Lazy<Api>
) {
    private val api by lazy { apiLazy.get() }
    suspend fun evaluate(
        tokens: List<EvaluationToken>,
        userId: String
    ): NetworkResponse<EvaluationResult, EvaluationErrorResponse> {
        return api.evaluate(createEvaluationRequest(tokens, userId))
    }

    suspend fun getEvaluations(request: GetEvaluationsRequest): NetworkResponse<GetEvaluationsResult, GetEvaluationsErrorResponse> {
        return api.getEvaluations(request)
    }

    private fun createEvaluationRequest(
        tokens: List<EvaluationToken>,
        userId: String
    ): EvaluationRequest {
        return EvaluationRequest(tokens
            .map { it.toJsonToken() }, userId
        )
    }
}
