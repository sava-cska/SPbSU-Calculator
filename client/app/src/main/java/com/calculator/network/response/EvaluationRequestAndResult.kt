package com.calculator.network.response

import com.calculator.network.request.EvaluationRequest
import com.calculator.network.request.Token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluationRequestAndResult(
    @Json(name = "Evaluation") val evaluation: List<Token>,
    @Json(name = "Result") val result: String?
)
