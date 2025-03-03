package com.shopfun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.shopfun.ui.theme.ShopFunTheme
import kotlinx.coroutines.launch

class GenerateOrder : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShopFunTheme {
                val context = LocalContext.current
                var products: List<Product>? = null
                val CartCheckedIds = remember { mutableStateOf<List<Int>>(emptyList()) } // State to store the list of selected IDs

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(0.dp, 50.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text("Selected Product IDs: ${CartCheckedIds.value.joinToString(", ")}")

                        var isClicked by remember { mutableStateOf(false) }

                        Button(
                            onClick = {
                                for (item in CartCheckedIds.value) {
                                    lifecycleScope.launch {
                                        dbPushToOrder(context, item)
                                        CartCheckedIds.value = emptyList()
                                        isClicked = !isClicked
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .width(200.dp)
                        ) {
                            Text("Generate Order")
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {
                            // Go to View Orders
                            Button(
                                onClick = {
                                    val intent = Intent(context, OrderDetails::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                            ) {
                                Text("View Orders")
                            }

                            // Go to Add Products
                            Button(
                                onClick = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                            ) {
                                Text("Go To Products")
                            }
                        }

                        LaunchedEffect(Unit) {
                                products = fetchProducts(context)
                                isClicked = !isClicked
                        }

                        // Populate Grid with Products added
                        if (isClicked == true)
                            ProductListGrid(products, CartCheckedIds)
                    }
                }
            }
        }
    }
}


suspend fun dbPushToOrder(
    context: Context,
    productid: Int
) {
    //Insert into Database entity Order
    val db = ShoppingRoomDB.getDatabase(context)
    val OrderDAO = db.daoOrder()

    val maxOrderId: Int = OrderDAO.getMaxOrderid()

    val Order = Order(order_id = maxOrderId + 1, product_id = productid)
    OrderDAO.insertOrder(Order)
}


@Composable
fun ProductListGrid(products: List<Product>?, CartCheckedIds: MutableState<List<Int>>) {
    if (!products.isNullOrEmpty())//&& products.isNotEmpty())//products.size > 0)
    // List of images or items to display in the grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 4 items per row
//            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(products) { product ->
                ProductListItem(product, CartCheckedIds)
            }
        }
}


@Composable
fun ProductListItem(product: Product, CartCheckedIds: MutableState<List<Int>>) {
//    val vertscrollState = rememberScrollState()
//    var isCartChecked by remember { mutableStateOf(false) }
//    val gradient = Brush. horizontalGradient(listOf(Color.Red, Color.Blue, Color.Green), 0.0f, 1000.0f, TileMode.Decal )

    Box(
        modifier = Modifier
            .padding(4.dp, 4.dp, 4.dp, 2.dp)
            .fillMaxSize()
//            .height(120.dp)
//            .background(brush = gradient)
//            .padding(10.dp)
//            .border(1.dp, Color.Blue)
            .background(Color(0xFFE2F4FE), shape = MaterialTheme.shapes.medium),
//            .scrollable(vertscrollState, Orientation.Vertical)
//            .verticalScroll(vertscrollState, true, null, true),
        contentAlignment = Alignment.Center,
    ) {

//        Text(text = content, style = MaterialTheme.typography.headlineMedium)
            Column(
                modifier = Modifier
                    .padding(0.dp)
//                    .border(5.dp, Color.Blue)
                    .padding(10.dp, 10.dp)
                    .fillMaxSize(),
//                    .fillMaxWidth(0.7f)
//                    .fillMaxHeight(1f),
//                horizontalAlignment = Alignment.Start
            )
            {

                /*
             // Load the product image
             Image(
                 painterResource(id = R.drawable.ic_launcher_foreground),
//            painter = rememberImagePainter(product.imageUrl),
                 contentDescription = product.name,
                 modifier = Modifier
                     .size(100.dp)
                     .clip(RoundedCornerShape(8.dp))
                     .background(MaterialTheme.colorScheme.primary)
             )

             Spacer(modifier = Modifier.height(8.dp))
*/

                // Product name
                Text(
                    text = product.Name.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Product Description
                Text(
                    text = product.Description.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Product price
                Text(
                    text = product.Price.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Left
                )
//            }

            Row(modifier = Modifier
                .padding(0.dp)
//                .border(2.dp,Color.Blue)
                .fillMaxSize(),
//                .fillMaxWidth(.3f)
//                .fillMaxHeight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(text = "Add Cart")//, Modifier.border(2.dp, SolidColor(Color.Blue), CutCornerShape(5.dp)))
                Checkbox(//modifier = Modifier.border(2.dp, SolidColor(Color.Blue), CutCornerShape(5.dp)),
                    checked = CartCheckedIds.value.contains(product.id),
                    onCheckedChange = {
                        // Update the list of selected IDs
                        CartCheckedIds.value = if (CartCheckedIds.value.contains(product.id)) {
                            CartCheckedIds.value - product.id // Remove ID if already selected
                        } else {
                            CartCheckedIds.value + product.id // Add ID if not selected
                        }
                    }
                )
            }

/*
        Text(
            "-- " + if (CartCheckedIds.value.contains(product.id)) "Added to Cart" else "Not in Cart" + " --",
//                "-- " + if (isCartChecked) "Added to Cart" else "Not in Cart" + " --",
            Modifier
                .padding(start = 5.dp)
//                    .border(2.dp, SolidColor(Color.Blue), CutCornerShape(0.dp))
        )
*/
        }
    }
}
