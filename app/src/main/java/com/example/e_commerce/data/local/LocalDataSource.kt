package com.example.e_commerce.data.local

import com.example.e_commerce.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val productDao: ProductDao
) {

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun insertProducts(list: List<Product>) {
        productDao.insertProducts(list)
    }

    fun getProducts(): Flow<List<Product>> {
        return productDao.getProducts()
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProduct() {
        productDao.deleteAllProduct()
    }

    fun searchProductOrCategory(query: String): Flow<List<Product>> {
        return productDao.searchProductOrCategory(query)
    }

}