package com.shopfun.RoomDatabaseMVVM

import androidx.lifecycle.LiveData

class mvvmRepositoryShopping(private val db: DBRoomShopping) {
    suspend fun repomvvmInsertProduct(product: mvvmProduct) = db.daomvvmProduct().insertProduct(product)

    fun repomvvmGetProduct() : LiveData<List<mvvmProduct>> = db.daomvvmProduct().getAllProducts()
}
