package com.shopfun

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shopfun.ui.theme.ShopFunTheme

class ServiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var blnButtonState by remember { mutableStateOf(true) }

            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }

            ShopFunTheme {
                Column(
                    modifier = Modifier.padding(0.dp,50.dp,0.dp,0.dp).height(150.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (blnButtonState) {
                                ContextCompat.startForegroundService(this@ServiceActivity,
                                    Intent(this@ServiceActivity, ServiceForeground::class.java)
                                )
//                                startService(
//                                    Intent(this@ServiceActivity, ServiceBackground::class.java)
//                                )
                            }
                            else
                            {
                                stopService(
                                    Intent(this@ServiceActivity, ServiceForeground::class.java)
                                )
/*
                                stopService(
                                    Intent(this@ServiceActivity, ServiceBackground::class.java)
                                )
*/
                            }
                            blnButtonState = !blnButtonState
                        }
                    )
                    {
                        if (blnButtonState)
                            Text("Start Service")
                        else
                            Text("Stop Service")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ShopFunTheme {
        Greeting2("Android")
    }
}