package com.example.e_commerce.data.local

import androidx.room.*
import com.example.e_commerce.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(list: List<Product>)

    @Query("SELECT * FROM products_table ORDER BY id ASC")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE title LIKE :query OR category LIKE :query ORDER BY id ASC")
    fun searchProductOrCategory(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE category LIKE :query ORDER BY id ASC")
    fun filterProductByCategory(query: String): Flow<List<Product>>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products_table")
    suspend fun deleteAllProduct()

}