package com.haljaa200.groceriesh.models

data class LoginResponse(
    val `data`: LoginResponseData,
    val success: Boolean
)

data class LoginResponseData(
    val message: String = "",
    val customer: Customer? = null,
    val token: String = "",
)