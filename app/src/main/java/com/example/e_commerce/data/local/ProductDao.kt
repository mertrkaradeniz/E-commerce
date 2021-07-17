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

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products_table")
    suspend fun deleteAllProduct()

}