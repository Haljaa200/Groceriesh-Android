package com.haljaa200.groceriesh.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

data class Order(
    val customer_id: String,
    val delivery_address: String,
    val delivery_latitude: Double,
    val delivery_longitude: Double,
    val delivery_time: Long,
    val delivery_time_planned: Long,
    val items: List<DefaultOrderItem>,
    var notes: String,
    val total_price: Double
): Serializable

data class DefaultOrderItem(
    val name: String,
    val price: Double,
    val quantity: Int,
    val unit: String
)

@Entity(
    tableName = "orderItems",
    indices = [Index(value = ["_id"], unique = true)]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val _id: String,
    val name: String,
    val photo: String,
    val price: Double,
    val quantity: Int,
    val unit: String
): Serializable