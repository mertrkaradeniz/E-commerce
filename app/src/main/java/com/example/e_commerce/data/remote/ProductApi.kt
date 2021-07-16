package com.example.e_commerce.data.remote

import com.example.e_commerce.data.model.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<Product>

}