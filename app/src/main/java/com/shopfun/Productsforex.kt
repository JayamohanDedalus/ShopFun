package com.shopfun

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Productsforex(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String,
    val description: String,
    val price: Float
)
