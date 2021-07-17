package com.example.e_commerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_commerce.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
}