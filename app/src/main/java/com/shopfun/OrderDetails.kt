package com.shopfun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shopfun.ui.theme.ShopFunTheme
import kotlinx.coroutines.launch

class OrderDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopFunTheme {
                val context = LocalContext.current
                var OrderByIds: List<Int>? = null

                Scaffold(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) { innerPadding ->
//                    DropdownExample()

                    Column(
                        modifier = Modifier.padding(0.dp, 50.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        var isClicked by remember { mutableStateOf(false) }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {
                            // Go to Add Products
                            Button(
                                onClick = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.padding(top = 4.dp).width(200.dp)
                            ) {
                                Text("Go To Products")
                            }

                            // Go to Order Products
                            Button(
                                onClick = {
                                    val intent = Intent(context, GenerateOrder::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.padding(top = 4.dp).width(200.dp)
                            ) {
                                Text("Order Products")
                            }
                        }

                        LaunchedEffect(Unit) {
                            OrderByIds = fetchAllOrderIds(context)
                            isClicked = !isClicked
                        }

                        // Populate Order Items in DropDown
                        if (isClicked == true)
                        {
                            ListOrders(context, OrderByIds)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListOrders(context : Context, OrderByIds : List<Int>?) {
    if (!OrderByIds.isNullOrEmpty()) {
        // State for controlling the visibility of the dropdown
        var expanded by remember { mutableStateOf(false) }
        // State to store the selected item
        var isClicked by remember { mutableStateOf(false) }
        val selectedOption = remember { mutableStateOf("") }
        var orderProductIds: List<Int>? by remember { mutableStateOf(null) }
        var orderProductItems: List<Product>? by remember { mutableStateOf(null) }

        Box(
            modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text(text = "Select Order")
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Select Order")
                    }
                    if (selectedOption.value != null && selectedOption.value != "") {
                        Text(color = Color.Blue, style = TextStyle(fontSize = 20.sp),
                            text = buildAnnotatedString {
                                append("Details for \"")
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold))
                                {
                                    append("Order - ${selectedOption.value}")
                                }
                                append("\"")
                            }
                        )
//                        Text(style = MaterialTheme.typography.bodyLarge, color = Color.Blue, text = "Viewing Details for ")
//                        Text(style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.Blue, text = "'Order - ${selectedOption.value}'")
                    }

                    // Get the coroutine scope for launching coroutines
                    val coroutineScope = rememberCoroutineScope()
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        OrderByIds.forEach { option ->
                            DropdownMenuItem(
                                text = { Text("Order - $option") },
                                onClick = {
                                    isClicked = false
                                    // Launch a coroutine to call the suspend function
                                    coroutineScope.launch {
                                        selectedOption.value = option.toString() // Set the selected option
                                        orderProductIds = fetchProductIdsByOrderId(context = context, selectedOption.value.toInt())
                                        orderProductItems = fetchOrderProductByIds(context = context, orderProductIds)
                                        expanded = false // Close the dropdown
                                        isClicked = true
//                                    isClicked = !isClicked
                                    }
                                }
                            )
                        }
                    }
                }

                if (isClicked)
                {
//                    if (orderProductIds != null)
//                        Text(orderProductIds!!.size.toString())
//                    if (orderProductItems != null)
//                        Text(orderProductItems!!.size.toString())

                    // Populate Grid with Order Items
                    ProductGrid(orderProductItems)
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

suspend fun fetchProductIdsByOrderId(context: Context, orderId: Int): List<Int> {
    //Get Order Item Details
    val db = ShoppingRoomDB.getDatabase(context)
    val orderDAO = db.daoOrder()

    val productIds: List<Int> = orderDAO.getProductIdsByOrderId(orderId)
    return productIds
}

suspend fun fetchOrderProductByIds(context: Context, orderProductIds: List<Int>?): List<Product> {
    //Get Order Item Details
    val db = ShoppingRoomDB.getDatabase(context)
    val productDAO = db.daoProduct()

    val orderProductItems: List<Product> = productDAO.getProductByIds(orderProductIds!!.toIntArray())
    return orderProductItems
}


/*//LongBasicDropdownMenu
@Composable
fun LongBasicDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    // Placeholder list of 100 strings for demonstration
    val menuItemData = List(100) { "Option ${it + 1}" }

    Box(modifier = Modifier.padding(16.dp).background(Color(0xFFE2F4FE), shape = MaterialTheme.shapes.medium))
    {
        Row (modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,)
        {
            Text("More Options")
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItemData.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {}
                )
            }
        }
    }
}
*/

/*//LongBasicDropdownMenu Preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LongBasicDropdownMenu()
}
*/
