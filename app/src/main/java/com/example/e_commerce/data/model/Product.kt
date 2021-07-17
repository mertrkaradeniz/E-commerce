package com.example.e_commerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_commerce.util.Constants.PRODUCTS_TABLE
import com.google.gson.annotations.SerializedName

@Entity(tableName = PRODUCTS_TABLE)
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("image")
    val image: String,
)