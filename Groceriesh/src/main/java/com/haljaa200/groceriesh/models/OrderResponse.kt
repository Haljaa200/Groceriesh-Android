package com.haljaa200.groceriesh.models

data class OrderResponse(
    val `data`: OrderResponseData,
    val success: Boolean
)

data class OrderResponseData(
    val order: OrderResponseObject? = null,
    val message: String = ""
)

data class OrderResponseObject(
    val customer_id: String,
    val delivery_address: String,
    val delivery_latitude: Double,
    val delivery_longitude: Double,
    val delivery_time: Long,
    val delivery_time_planned: Long,
    val items: List<OrderResponseItem>,
    val notes: String,
    val total_price: Double
)

data class OrderResponseItem(
    val name: String,
    val photo: String,
    val price: Double,
    val quantity: Int,
    val unit: String
)