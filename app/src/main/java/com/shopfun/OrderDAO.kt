package com.shopfun

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDAO {
    @Query("SELECT * FROM 'Order'")
    suspend fun getAllOrders(): List<Order>

    @Query("SELECT product_id FROM 'Order' WHERE order_id = (:orderId)")
    suspend fun getProductIdsByOrderId(orderId : Int): List<Int>

    @Query("SELECT DISTINCT order_id FROM 'Order'")
    suspend fun getAllOrderIds(): List<Int>

//    @Query("SELECT * FROM Order WHERE id IN (:orderIds)")
//    suspend fun getAllOrdersById(orderIds: IntArray): List<Order>

    @Query("SELECT max(order_id) FROM 'Order'")
    suspend fun getMaxOrderid(): Int

    @Insert
    suspend fun insertOrder(order: Order)

    /*@Update
    fun updateUsers(vararg order: Order)*/

    /* @Delete
     fun delete(order: Order)*/
//    suspend fun delete(Order: Order)
}