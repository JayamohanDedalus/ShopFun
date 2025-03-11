package com.shopfun.RoomDatabaseMVVM

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface mvvmProductDAO {
    @Query("SELECT * FROM mvvmProduct")
    fun getAllProducts(): LiveData<List<mvvmProduct>>

    @Query("SELECT * FROM mvvmProduct WHERE id IN (:productIds)")
    fun getProductByIds(productIds: IntArray): LiveData<List<mvvmProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(products: mvvmProduct)

    /*@Update
    fun updateUsers(vararg products: Product)*/
//    suspend fun updateUsers(vararg products: Product)

    /* @Delete
     fun delete(product: Product)*/
//    suspend fun delete(product: Product)
}