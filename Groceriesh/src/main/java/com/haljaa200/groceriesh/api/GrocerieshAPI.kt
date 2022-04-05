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
}