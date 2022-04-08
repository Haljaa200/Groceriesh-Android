package com.haljaa200.groceriesh.models

data class SingleOrderResponse(
    val `data`: SingleOrderResponseData,
    val success: Boolean
)

data class SingleOrderResponseData(
    val order: OrdersResponseObject
)