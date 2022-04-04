package com.haljaa200.groceriesh.models

data class RegisterResponse(
    val `data`: RegisterResponseData,
    val success: Boolean
)

data class RegisterResponseData(
    val customer: Customer? = null,
    val token: String = "",
    val message: String = ""
)