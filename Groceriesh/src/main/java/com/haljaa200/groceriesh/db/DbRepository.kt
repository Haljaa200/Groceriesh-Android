package com.haljaa200.groceriesh.db

import com.haljaa200.groceriesh.models.OrderItem

class DbRepository(val db: GrocerieshDatabase) {

    suspend fun upsertOrderItem(orderItem: OrderItem) = db.getBasketDao().upsert(orderItem)
    suspend fun updateOrderItemQuantity(id: String, quantity: Int) = db.getBasketDao().updateOrderItemQuantity(id, quantity)
    fun getOrderItem(id: String) = db.getBasketDao().getOrderItem(id)
    fun getBasketOrders() = db.getBasketDao().getBasketOrders()
    suspend fun deleteOrderItem(orderItem: OrderItem) = db.getBasketDao().deleteOrderItem(orderItem)
    suspend fun deleteOrderItem(id: String) = db.getBasketDao().deleteOrderItem(id)
    suspend fun deleteBasket() = db.getBasketDao().deleteBasket()
    fun getBasketSum() = db.getBasketDao().getBasketSum()
}