package com.example.e_commerce.data.remote

import com.example.e_commerce.data.model.Product
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val productApi: ProductApi
){

    suspend fun getProducts(): List<Product> = productApi.getProducts()

}