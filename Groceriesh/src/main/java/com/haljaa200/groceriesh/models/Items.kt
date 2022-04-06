package com.haljaa200.groceriesh.models

import java.io.Serializable

data class Items(
    val `data`: ItemsData,
    val success: Boolean
)

data class ItemsData(
    val items: List<Item>
)

data class Item(
    val __v: Int,
    val _id: String,
    val category_id: String,
    val description: String,
    val photo: String = "",
    val name: String,
    val price: Double,
    val unit: String,
    val vendor_id: String
): Serializable