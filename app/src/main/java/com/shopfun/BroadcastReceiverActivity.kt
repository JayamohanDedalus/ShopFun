package com.shopfun

import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.shopfun.ui.theme.ShopFunTheme

class BroadcastReceiverActivity : ComponentActivity() {
    private var broadcastEventReceiver : BroadcastSysEventReceiver? = null
    private var broadcastCustomReceiver : BroadcastCustomReceiver? = null
    private var broadcastSMSReceiver : BroadcastSMSReceiver? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS), 101)
            }

            ShopFunTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier =  Modifier.padding(innerPadding))
                    {

                        //Register WIFI_STATE_CHANGED_ACTION & ACTION_AIRPLANE_MODE_CHANGED
                        Button(
                            onClick = {
                                broadcastEventReceiver = BroadcastSysEventReceiver()
/*
                                registerReceiver(broadcastEventReceiver,
                                    IntentFilter(android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION))
                                registerReceiver(broadcastEventReceiver,
                                    IntentFilter(android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED))
*/
                                val intentFilter = IntentFilter()
                                intentFilter.addAction(android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION)
                                intentFilter.addAction(android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED)
                                registerReceiver(broadcastEventReceiver, intentFilter)
                                Toast.makeText(this@BroadcastReceiverActivity, "Registered WIFI & Airplane Mode", Toast.LENGTH_LONG).show()
                            }
                        ) {
                            Text("Send WIFI/Airplane Mode System Events")
                        }

                        //Register CUSTOM_BROADCAST
                        Button(
                            onClick = {
                                broadcastCustomReceiver = BroadcastCustomReceiver()
                                registerReceiver(broadcastCustomReceiver,
                                    IntentFilter("com.shopfun.broadcast.MY_CUSTOM_BROADCAST"),
                                    RECEIVER_EXPORTED
                                )
                                Toast.makeText(this@BroadcastReceiverActivity, "Registered Custom Broadcast", Toast.LENGTH_LONG).show()
                            }
                        )
                        {
                            Text("Register Custom Broadcast")
                        }

                        //SEND CUSTOM_BROADCAST MESSAGE Once Registered
                        Button(
                            onClick = {
                                val intent = Intent("com.shopfun.broadcast.MY_CUSTOM_BROADCAST")
                                intent.putExtra("message", "Custom message triggered")
                                sendBroadcast(intent)
                            }
                        )
                        {
                            Text("Get Custom Broadcast message")
                        }

                        //SMS_RECEIVED_ACTION
                        Button(
                            onClick = {
                                broadcastSMSReceiver = BroadcastSMSReceiver()
                                val intentFilter = IntentFilter()
                                intentFilter.addAction(android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
                                registerReceiver(broadcastSMSReceiver, intentFilter, RECEIVER_EXPORTED)
                                Toast.makeText(this@BroadcastReceiverActivity, "Registered SMS_RECEIVED_ACTION", Toast.LENGTH_LONG).show()
                            }
                        ) {
                            Text("Register SMS_Receive")
                        }

/*
                        // Send SMS
                        Button(
                            onClick = {
                                val smsManager : SmsManager = SmsManager.getDefault()
                                smsManager.sendTextMessage("9876543210", null,
                                    "Test SMS Message", null, null)
                            }
                        ) {
                            Text("Send SMS")
                        }
*/
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadcastEventReceiver != null)
            unregisterReceiver(broadcastEventReceiver)

        if (broadcastCustomReceiver != null)
            unregisterReceiver(broadcastCustomReceiver)

        if (broadcastSMSReceiver != null)
            unregisterReceiver(broadcastSMSReceiver)
    }
}

