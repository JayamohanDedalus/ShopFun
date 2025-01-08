package com.shopfun

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class Product(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
//    val Name: String?,
//    val Description: String?,
//    val Price: Float?
    @ColumnInfo(name = "product_Name") val Name: String?,
    @ColumnInfo(name = "product_Description") val Description: String?,
    @ColumnInfo(name = "product_Price") val Price: Double
)
