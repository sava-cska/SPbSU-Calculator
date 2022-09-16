package com.calculator.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetEvaluationsRequest (
    @Json(name = "UserUid") val user_uid: String
)
