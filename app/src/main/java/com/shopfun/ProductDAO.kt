package com.shopfun

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDAO {
    @Query("SELECT * FROM Product")
    suspend fun getAllProducts(): List<Product>
//    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM Product WHERE id IN (:productIds)")
    suspend fun getProductByIds(productIds: IntArray): List<Product>

    @Insert
    suspend fun insertProduct(products: Product)
//    suspend fun insertAll(vararg products: Product)

    /*@Update
    fun updateUsers(vararg products: Product)*/
//    suspend fun updateUsers(vararg products: Product)

   /* @Delete
    fun delete(product: Product)*/
//    suspend fun delete(product: Product)
}