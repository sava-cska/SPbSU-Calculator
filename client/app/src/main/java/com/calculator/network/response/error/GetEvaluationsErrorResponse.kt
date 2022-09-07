package com.calculator.network.response.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GetEvaluationsErrorResponse(
    @Json(name = "get_evaluations_errors") val errors: List<NetError>,
)
