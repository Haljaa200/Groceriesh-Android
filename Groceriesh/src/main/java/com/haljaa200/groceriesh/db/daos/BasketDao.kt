package com.haljaa200.groceriesh.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.haljaa200.groceriesh.models.OrderItem

@Dao
interface BasketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(orderItem: OrderItem): Long

    @Query("UPDATE orderitems SET quantity = :quantity WHERE _id = :id")
    suspend fun updateOrderItemQuantity(id: String, quantity: Int)

    @Query("SELECT * FROM orderItems WHERE _id = :id")
    fun getOrderItem(id: String): LiveData<OrderItem>

    @Query("SELECT * FROM orderItems")
    fun getBasketOrders(): LiveData<List<OrderItem>>

    @Query("SELECT SUM(price *  quantity) FROM orderItems")
    fun getBasketSum(): LiveData<Double>

    @Query("DELETE FROM orderItems")
    suspend fun deleteBasket()

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    @Query("DELETE FROM orderItems WHERE _id = :id")
    suspend fun deleteOrderItem(id: String)

}