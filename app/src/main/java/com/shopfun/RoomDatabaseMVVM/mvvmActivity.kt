package com.shopfun.RoomDatabaseMVVM

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shopfun.Product
import com.shopfun.RoomDatabaseMVVM.ui.theme.ShopFunTheme
import com.shopfun.ShoppingRoomDB
import kotlinx.coroutines.launch

class mvvmActivity : ComponentActivity() {

    private val mvvmConfigViewModel by viewModels<mvvmConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = DBRoomShopping.getDatabase(applicationContext)
        val repository = database?.let { mvvmRepositoryShopping(it) }
        val factory = repository?.let { mvvmViewModelFactory(it) }
        val productViewModel: mvvmViewModelShopping? = factory?.let {mvvmViewModelFactory ->
            ViewModelProvider(
                this,
                mvvmViewModelFactory
            )
        }?.get(mvvmViewModelShopping::class.java)

        setContent {
            ShopFunTheme {
//                var productName by remember { mutableStateOf("") }
//                var productDescription by remember { mutableStateOf("") }
//                var productPrice by remember { mutableStateOf("") }
//                val context = LocalContext.current
//                var products: List<mvvmProduct>? = null

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    productViewModel?.let { productViewModel ->
                        ProductForm(mvvmConfigViewModel, productViewModel)
                    }
                }
            }
        }
    }

    @Composable
    private fun ProductForm(configViewModel: mvvmConfigViewModel, productViewModel: mvvmViewModelShopping) {
        val context = LocalContext.current
//        var products: List<mvvmProduct>? = null

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
                    value = configViewModel.productName,
                    label = { Text(text = "Product Name") },
                    placeholder = { Text(text = "Enter Product Name") },
                    onValueChange = { configViewModel.productName = it },
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
                    value = configViewModel.productDescription,
                    label = { Text(text = "Product Description") },
                    placeholder = { Text(text = "Enter Product Description") },
                    onValueChange = { configViewModel.productDescription = it }
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
                    value = configViewModel.productPrice,
                    label = { Text(text = "Product Price") },
                    placeholder = { Text(text = "Enter Product Price") },
                    onValueChange = { configViewModel.productPrice = it }
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
            {
                // Add Product
                Button(
                    onClick = {
                        lifecycleScope.launch {
                            dbPushProductToDB(
                                productViewModel,
                                context,
                                configViewModel.productName,
                                configViewModel.productDescription,
                                configViewModel.productPrice.toDouble()
                            )
                            configViewModel.productName = ""
                            configViewModel.productDescription = ""
                            configViewModel.productPrice = ""
                        }
                    },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(200.dp)
                ) {
                    Text("Add Product")
                }

                configViewModel.products = fetchAllProducts(productViewModel, context)
                Button(
                    onClick = {
                        lifecycleScope.launch {
                            configViewModel.products
                        }
                    },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(200.dp)
                ) {
                    Text("View Products")
                }
            }

            // Populate Grid with Products added
            ProductLazyGrid(configViewModel.products)
        }
    }
}

fun dbPushProductToDB(
    productViewModel: mvvmViewModelShopping,
    context: Context,
    productName: String,
    productDescription: String,
    productPrice: Double
) {
    val product =
        mvvmProduct(Name = productName, Description = productDescription, Price = productPrice ?: 0.0)
    productViewModel.InsertProduct(product)
}

@Composable
fun fetchAllProducts(productViewModel: mvvmViewModelShopping, context: Context): List<mvvmProduct> {
    //Get Product Details
    val allProducts by productViewModel.products.observeAsState(initial = emptyList())
    return allProducts
}

@Composable
fun ProductLazyGrid(products: List<mvvmProduct>?) {
    if (!products.isNullOrEmpty())//&& products.isNotEmpty())//products.size > 0)
    // List of images or items to display in the grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 4 items per row
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.9f).padding(top = 0.dp)
        ) {
            items(products) { product ->
                ProductItemList(product)
            }
        }
}

@Composable
fun ProductItemList(product: mvvmProduct) {
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
