package com.haljaa200.groceriesh.models

data class Categories(
    val `data`: CategoriesData,
    val success: Boolean
)

data class CategoriesData(
    val categories: List<Category>
)

data class Category(
    val __v: Int,
    val _id: String,
    val name: String
)