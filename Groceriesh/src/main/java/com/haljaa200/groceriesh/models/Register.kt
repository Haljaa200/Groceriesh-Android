package com.haljaa200.groceriesh.models

data class Register(
    val delivery_address: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val latitude: Double,
    val longitude: Double,
    val password: String,
    val phone: String
)