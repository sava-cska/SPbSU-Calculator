package com.calculator.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "Type") val type : String,
    @Json(name = "Body") val body : String

) {
    companion object {
        val NUMBER_TYPE = "Number"
        val OPERATION_TYPE = "Operation"
        val PARENTHESIS_TYPE = "Parenthesis"
    }
}

