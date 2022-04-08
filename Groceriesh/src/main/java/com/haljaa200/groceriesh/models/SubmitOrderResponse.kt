package com.haljaa200.groceriesh.models

data class SubmitOrderResponse(
    val `data`: SubmitOrderResponseData,
    val success: Boolean
)

data class SubmitOrderResponseData(
    val order: SubmitOrderResponseObject? = null,
    val message: String = ""
)

data class SubmitOrderResponseObject(
    val _id: String,
    val customer_id: String,
    val delivery_address: String,
    val delivery_latitude: Double,
    val delivery_longitude: Double,
    val delivery_time: Long,
    val delivery_time_planned: Long,
    val items: List<SubmitOrderResponseItem>,
    val notes: String,
    val total_price: Double
)

data class SubmitOrderResponseItem(
    val name: String,
    val photo: String,
    val price: Double,
    val quantity: Int,
    val unit: String
)