package com.calculator.network.response.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetError(
    @Json(name = "code") val code : String,
    @Json(name = "message") val message : String
)
