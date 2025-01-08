package com.shopfun

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDaoForEx {
    @Insert
    suspend fun insertProduct(product: Productsforex)

//    @Query("SELECT * FROM Productsforex")
//    suspend fun fetchAll(): List<Productsforex>
}