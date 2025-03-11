package com.shopfun

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BroadcastSMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        Log.d("ShopFun-BC-SMS", "onReceive: SMS")

        if (android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION == action) {
            for (sms in android.provider.Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                Toast.makeText(
                    context,
                    "originatingAddress: ${sms.originatingAddress} and MessageBody: ${sms.messageBody}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}