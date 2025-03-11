package com.shopfun.RoomDatabaseMVVM

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mvvmProduct")
data class mvvmProduct(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "product_Name") val Name: String?,
    @ColumnInfo(name = "product_Description") val Description: String?,
    @ColumnInfo(name = "product_Price") val Price: Double
)
