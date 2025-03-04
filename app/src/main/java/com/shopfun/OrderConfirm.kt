package com.shopfun

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shopfun.ui.theme.ShopFunTheme

class OrderConfirm : ComponentActivity() {
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val data = intent.getStringExtra("Order Confirmation")
            Toast.makeText(context, data, Toast.LENGTH_LONG).show()
            Log.d("OrderConfirm", "Received data: $data")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContent {
            ShopFunTheme {
                Text("Order Confirm")
            }
        }

        // Register the receiver to listen for the broadcast
        val filter = IntentFilter("com.example.ACTION_BROADCAST")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mReceiver, filter, RECEIVER_EXPORTED)
        }
        Log.d("OrderConfirm", "Service Registered")

/*
        val serviceIntent = Intent(this, ServiceBroadcast::class.java)
        startService(serviceIntent)
*/
/*
        enableEdgeToEdge()
        setContent {
            ShopFunTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
*/
    }

    override fun onStart() {
        super.onStart()
/*
        val serviceIntent = Intent(this, ServiceBroadcast::class.java)
        startService(serviceIntent)
*/
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
/*
        // Register the receiver to listen for the broadcast
        val filter = IntentFilter("com.example.ACTION_BROADCAST")
        registerReceiver(mReceiver, filter, RECEIVER_NOT_EXPORTED)
        Log.d("OrderConfirm", "Service Registered")
*/
    }

    override fun onPause() {
        super.onPause()
        // Unregister the receiver when not needed
        unregisterReceiver(mReceiver)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopFunTheme {
        Greeting("Android")
    }
}