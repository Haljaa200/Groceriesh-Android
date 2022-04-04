package com.haljaa200.groceriesh.api

import com.haljaa200.groceriesh.models.Login
import com.haljaa200.groceriesh.models.LoginResponse
import com.haljaa200.groceriesh.models.Register
import com.haljaa200.groceriesh.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.*

interface GrocerieshAPI {
    @POST("customer/register")
    suspend fun register(@Body data: Register): Response<RegisterResponse>

    @POST("customer/login")
    suspend fun login(@Body data: Login): Response<LoginResponse>
}