package com.shopfun.RoomDatabaseMVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class mvvmViewModelFactory(private val repoShopping: mvvmRepositoryShopping) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mvvmViewModelShopping::class.java)) {
            return mvvmViewModelShopping(repoShopping) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}