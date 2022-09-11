package com.calculator.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluationResult(
    @Json(name = "Result") val result: String
)
