package com.shopfun

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shopfun.ui.theme.ShopFunTheme

class CPContentProviderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopFunTheme {
//                var quotesList : List<String> ? = emptyList()
                var quotesList: List<String> by remember { mutableStateOf(emptyList()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)) {
                        FetchData(
                            name = "Get CP DB Details",
                            onClick = { quotesList = getData() },
                            modifier = Modifier.padding(innerPadding)
                        )

                        LazyColumn {
                            if(quotesList.isNotEmpty()) {
                                items(quotesList.size) {idx ->
                                    Text(
                                        text = quotesList[idx],
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getData() : List<String> {
        // Use Content Resolver to query the Content Provider
        val cursor = contentResolver.query(
            CPContentProvider.CONTENT_URI,
            null, null, null, null
        )

        // Read quotes from the cursor and display them
        val quotesList = mutableListOf<String>()
        cursor?.let {
            while (it.moveToNext()) {
                val quote = it.getString(it.getColumnIndex("quote"))
                quotesList.add(quote)
            }
            it.close()
        }

        Log.d("CPContentProviderActivity", "getData: quotesList $quotesList")

        return quotesList
    }
}

@Composable
fun FetchData(name: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = "$name!"
        )
    }
}

