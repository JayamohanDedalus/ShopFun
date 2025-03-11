package com.shopfun.RoomDatabaseMVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class mvvmViewModelShopping(private val repoShopping: mvvmRepositoryShopping) : ViewModel() {

    val products : LiveData<List<mvvmProduct>> = repoShopping.repomvvmGetProduct()

    fun InsertProduct(product: mvvmProduct?) {
        viewModelScope.launch {
            product?.let { repoShopping.repomvvmInsertProduct(it) }
        }
    }

}