package com.shopfun

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint.Align
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.shopfun.ui.theme.ShopFunTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopFunTheme {
                var productName by remember { mutableStateOf("") }
                var productDescription by remember { mutableStateOf("") }
                var productPrice by remember { mutableStateOf("") }
                val context = LocalContext.current
                var products: List<Product>? = null

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        // Product Name
                        Row(
                            modifier = Modifier.padding(10.dp, 0.dp, 5.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(modifier = Modifier.width(160.dp), text = "Product Name")
                            OutlinedTextField(
                                modifier = Modifier.width(260.dp),
                                value = productName,
                                label = { Text(text = "Product Name") },
                                placeholder = { Text(text = "Enter Product Name") },
                                onValueChange = { productName = it },
                            )
                        }

                        // Product Description
                        Row(
                            modifier = Modifier.padding(10.dp, 0.dp, 5.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(modifier = Modifier.width(160.dp), text = "Product Description")
                            OutlinedTextField(modifier = Modifier.width(260.dp),
                                value = productDescription,
                                label = { Text(text = "Product Description") },
                                placeholder = { Text(text = "Enter Product Description") },
                                onValueChange = { productDescription = it }
                            )
                        }

                        // Product Price
                        Row(
                            modifier = Modifier.padding(10.dp, 0.dp, 5.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(modifier = Modifier.width(160.dp), text = "Product Price")
                            OutlinedTextField(modifier = Modifier.width(260.dp),
                                value = productPrice,
                                label = { Text(text = "Product Price") },
                                placeholder = { Text(text = "Enter Product Price") },
                                onValueChange = { productPrice = it }
                            )
                        }

                        var isClicked by remember { mutableStateOf(false) }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {
                            // Add Product
                            Button(
                                onClick = {
//                                isClicked = false
                                    lifecycleScope.launch {
                                        dbPushToProduct(
                                            context,
                                            productName,
                                            productDescription,
                                            if (productPrice == null) 0.0 else productPrice.toDouble()
                                        )
                                        productName = ""
                                        productDescription = ""
                                        productPrice = ""
//                                    isClicked = true
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                            ) {
                                Text("Add Product")
                            }

                            Button(
                                onClick = {
                                    isClicked = false
                                    lifecycleScope.launch {
                                        products = fetchProducts(context)
//                                    isClicked = !isClicked
                                        isClicked = true
                                    }
//                                isClicked = false
                                },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                            ) {
                                Text("View Products")
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
                        {
                            // Go to Order Products
                            Button(
                                onClick = {
                                    val intent = Intent(context, GenerateOrder::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .width(200.dp)
                            ) {
                                Text("Order Products")
                            }

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
                        }

                        // Populate Grid with Products added
                        if (isClicked == true) {
                            ProductGrid(products)
//                            isClicked = false
                        }
                    }
                }
            }
        }
    }
}

//@Composable
suspend fun dbPushToProduct(
    context: Context,
    productName: String,
    productDescription: String,
    productPrice: Double
) {
    //Insert into Database entity Product
    val db = ShoppingRoomDB.getDatabase(context)
    val productDAO = db.daoProduct()

    val product =
        Product(Name = productName, Description = productDescription, Price = productPrice ?: 0.0)
    productDAO.insertProduct(product)
}

suspend fun fetchProducts(context: Context): List<Product> {
    //Get Product Details
    val db = ShoppingRoomDB.getDatabase(context)
    val productDAO = db.daoProduct()

    val allProducts: List<Product> = productDAO.getAllProducts()
    return allProducts
}

@Composable
fun ProductGrid(products: List<Product>?) {
    if (!products.isNullOrEmpty())//&& products.isNotEmpty())//products.size > 0)
    // List of images or items to display in the grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 4 items per row
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.9f).padding(top = 0.dp)
        ) {
            items(products) { product ->
                ProductItem(product)
            }
        }
}

@Composable
fun ProductItem(product: Product) {
    val vertscrollState = rememberScrollState()
//    val gradient = Brush. horizontalGradient(listOf(Color.Red, Color.Blue, Color.Green), 0.0f, 1000.0f, TileMode.Decal )
    Box(
        modifier = Modifier
            .padding(4.dp, 4.dp, 4.dp, 2.dp)
            .fillMaxWidth()
            .height(120.dp)
//            .background(brush = gradient)
            .background(Color(0xFFE2F4FE), shape = MaterialTheme.shapes.medium)
            .padding(4.dp)
            .scrollable(vertscrollState, Orientation.Vertical)
            .verticalScroll(vertscrollState, true, null, true),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

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
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Product Description
            Text(
                text = product.Description.toString(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Product price
            Text(
                text = product.Price.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
