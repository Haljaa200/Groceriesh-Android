package com.haljaa200.groceriesh.models

data class Customer(
    val _id: String,
    val delivery_address: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
)