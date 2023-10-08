package com.jvr.datovaschranka.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jvr.datovaschranka.services.NotificationService

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // BOOT_COMPLETED” start Service
        if (intent.action == ACTION) {
            //Service
            val serviceIntent = Intent(context, NotificationService::class.java)
            context.startService(serviceIntent)
        }
    }

    companion object {
        const val ACTION = "android.intent.action.BOOT_COMPLETED"
    }
}