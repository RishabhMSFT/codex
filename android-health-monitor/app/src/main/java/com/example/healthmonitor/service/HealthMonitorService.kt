package com.example.healthmonitor.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.healthmonitor.R
import com.example.healthmonitor.data.HealthMetricsRepository
import com.example.healthmonitor.worker.HourlyUploadWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HealthMonitorService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var repository: HealthMetricsRepository

    override fun onCreate() {
        super.onCreate()
        repository = HealthMetricsRepository(applicationContext)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        startMetricSamplingLoop()
        scheduleHourlyUploads(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startMetricSamplingLoop() {
        serviceScope.launch {
            while (isActive) {
                repository.saveCurrentMetric()
                delay(SAMPLING_INTERVAL_MS)
            }
        }
    }

    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.monitor_running))
            .setOngoing(true)
            .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Health monitor",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "health_monitor_channel"
        private const val NOTIFICATION_ID = 2001
        private const val SAMPLING_INTERVAL_MS = 60_000L
        private const val DEFAULT_SERVER_URL = "https://your-server.example.com/metrics"

        fun start(context: Context) {
            val intent = Intent(context, HealthMonitorService::class.java)
            context.startForegroundService(intent)
        }

        fun scheduleHourlyUploads(context: Context, serverUrl: String = DEFAULT_SERVER_URL) {
            val request = PeriodicWorkRequestBuilder<HourlyUploadWorker>(1, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(workDataOf(HourlyUploadWorker.KEY_SERVER_URL to serverUrl))
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                HourlyUploadWorker.UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }
}
