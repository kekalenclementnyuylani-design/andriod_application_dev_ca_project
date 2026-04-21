package com.ecostep.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ecostep.util.TrackingManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            TrackingManager.isTracking = false
        }
    }
}
