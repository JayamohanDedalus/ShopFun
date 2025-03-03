package com.shopfun

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shopfun.ui.theme.ShopFunTheme

class OrderProductDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var OrderByIds: List<Int>? = null
            ShopFunTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LongBasicDropdownMenu()

                    Column(
//                        modifier = Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp),
                        modifier = Modifier.padding(0.dp, 50.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        var isClicked by remember { mutableStateOf(false) }

                        /*//Generate Order Button
                        Button(
                            onClick = {
                                for (item in CartCheckedIds.value) {
                                    lifecycleScope.launch {
                                        dbPushToOrder(context, item)
                                        isClicked = !isClicked
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth(.5f)
                        ) {
                            Text("Generate Order")
                        }
*/

                        LaunchedEffect(Unit) {
                            OrderByIds = fetchAllOrderIds(context)
                            isClicked = !isClicked
                        }

                        /*
                                                Button(
                                                    onClick = {
                                                        isClicked = !isClicked
                        //                                isClicked = true
                                                        lifecycleScope.launch {
                                                            Orders = fetchOrders(context)
                                                            isClicked = !isClicked
                                                        }
                        //                                isClicked = false
                                                    },
                                                    modifier = Modifier
                                                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                                        .fillMaxWidth(.5f)
                                                ) {
                                                    Text("Show Orders")
                                                }
                        */

                        // Populate Grid with Order Items
                        if (isClicked == true)
                        {
                            Text("Test")
                            ListOrders(context, OrderByIds)
                        }
                    }

                }
            }
        }
    }
}

suspend fun fetchAllOrderIds(context: Context): List<Int> {
    //Get All Order
    val db = ShoppingRoomDB.getDatabase(context)
    val orderDAO = db.daoOrder()

    val orderById: List<Int> = orderDAO.getAllOrderIds()
    return orderById
}
