package com.calculator.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "type") val type : String,
    @Json(name = "body") val body : String
) {
    companion object {
        val NUMBER_TYPE = "Number"
        val OPERATION_TYPE = "Operation"
        val PARENTHESIS_TYPE = "Parenthesis"
    }
}
