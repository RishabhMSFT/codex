package com.example.healthmonitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.healthmonitor.service.HealthMonitorService

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            HealthMonitorService.start(context)
        }
    }
}
