package com.haljaa200.groceriesh.models

data class Vendors(
    val `data`: VendorsData,
    val success: Boolean
)

data class VendorsData(
    val vendors: List<Vendor>
)

data class Vendor(
    val _id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val store_name: String,
    val store_phone: String
)