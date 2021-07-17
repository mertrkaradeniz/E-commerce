package com.example.e_commerce.data.local

import androidx.room.Database
import com.example.e_commerce.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase {

    abstract fun productDao(): ProductDao
}