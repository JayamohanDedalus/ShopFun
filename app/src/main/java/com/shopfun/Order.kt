package com.shopfun

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class Order(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "order_id") val order_id: Int?,
    @ColumnInfo(name = "product_id") val product_id: Int?
)
