package com.haljaa200.groceriesh.api

import com.haljaa200.groceriesh.models.*
import retrofit2.Response
import retrofit2.http.*

interface GrocerieshAPI {
    @POST("customer/register")
    suspend fun register(@Body data: Register): Response<RegisterResponse>

    @POST("customer/login")
    suspend fun login(@Body data: Login): Response<LoginResponse>

    @PUT("customer/profile")
    suspend fun editProfile(@Header("x-auth-token") token: String, @Body data: Register): Response<RegisterResponse>

    @GET("customer/categories")
    suspend fun getCategories(@Header("x-auth-token") token: String): Response<Categories>

    @GET("customer/items")
    suspend fun getItems(@Header("x-auth-token") token: String): Response<Items>

    @GET("customer/category_items/{id}")
    suspend fun getCategoryItems(@Header("x-auth-token") token: String, @Path("id") categoryId: String): Response<Items>

    @GET("customer/vendor_items/{vendor_id}/{category_id}")
    suspend fun getVendorItems(@Header("x-auth-token") token: String, @Path("vendor_id") vendorId: String, @Path("category_id") categoryId: String): Response<Items>

    @GET("customer/vendors")
    suspend fun getVendors(@Header("x-auth-token") token: String): Response<Vendors>

    @POST("customer/order")
    suspend fun submitOrder(@Header("x-auth-token") token: String, @Body data: Order): Response<SubmitOrderResponse>

    @GET("customer/orders")
    suspend fun getOrders(@Header("x-auth-token") token: String): Response<OrdersResponse>

    @GET("customer/orders/{order_id}")
    suspend fun getOrder(@Header("x-auth-token") token: String, @Path("order_id") orderId: String): Response<SingleOrderResponse>
}