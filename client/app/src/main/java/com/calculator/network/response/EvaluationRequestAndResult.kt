package com.calculator.network.response

import com.calculator.network.request.EvaluationRequest
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluationRequestAndResult(
    @Json(name = "Evaluation") val request: EvaluationRequest,
    @Json(name = "Result") val result: String?
)
