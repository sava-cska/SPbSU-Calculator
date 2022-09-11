package com.calculator.network.response

import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GetEvaluationsResult(
    @Json(name = "Response") val evaluations: List<EvaluationRequestAndResult>,
)
