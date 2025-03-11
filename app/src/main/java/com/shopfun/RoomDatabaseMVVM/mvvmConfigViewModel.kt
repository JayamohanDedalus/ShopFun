package com.shopfun.RoomDatabaseMVVM

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class mvvmConfigViewModel : ViewModel() {
    var productName by mutableStateOf("")
    var productDescription by mutableStateOf("")
    var productPrice by mutableStateOf("")
//    var products : List<mvvmProduct> by mutableStateOf(emptyList())
}
