package com.calculator.network.response

import com.calculator.network.request.EvaluationRequest
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluationRequestAndResult(
    @Json(name = "evaluation_request") val request: EvaluationRequest,
    @Json(name = "evaluation_result") val result: String
)
