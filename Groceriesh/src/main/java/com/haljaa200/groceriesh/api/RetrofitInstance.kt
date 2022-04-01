package com.haljaa200.groceriesh.api

import retrofit2.Retrofit
import javax.inject.Inject

class RetrofitInstance @Inject constructor(val retrofit: Retrofit) {
    val api by lazy { retrofit.create(GrocerieshAPI::class.java) }
}