package com.haljaa200.groceriesh.models

data class OrdersResponse(
    val `data`: OrdersResponseData,
    val success: Boolean
)

data class OrdersResponseData(
    val orders: List<OrdersResponseObject>
)

data class OrdersResponseObject(
    val __v: Int,
    val _id: String,
    val customer_id: String,
    val delivery_address: String,
    val delivery_latitude: Double,
    val delivery_longitude: Double,
    val delivery_time: Long,
    val delivery_time_planned: Long,
    val items: List<OrdersResponseItem>,
    val notes: String,
    val total_price: Double
)

data class OrdersResponseItem(
    val _id: String,
    val name: String,
    val photo: String?,
    val price: Double,
    val quantity: Int,
    val unit: String
)