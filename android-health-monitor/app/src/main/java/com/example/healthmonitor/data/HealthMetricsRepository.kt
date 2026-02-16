package com.example.healthmonitor.data

import android.app.ActivityManager
import android.content.Context

class HealthMetricsRepository(private val context: Context) {
    private val dao = AppDatabase.getInstance(context).healthMetricDao()

    suspend fun saveCurrentMetric() {
        val runtime = Runtime.getRuntime()
        val appUsedMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val appMaxMb = runtime.maxMemory() / (1024 * 1024)

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val metric = HealthMetric(
            timestampMs = System.currentTimeMillis(),
            appUsedMemoryMb = appUsedMb,
            appMaxMemoryMb = appMaxMb,
            systemAvailMemoryMb = memoryInfo.availMem / (1024 * 1024),
            systemTotalMemoryMb = memoryInfo.totalMem / (1024 * 1024),
            lowMemory = memoryInfo.lowMemory
        )
        dao.insert(metric)
    }

    suspend fun getLastHourMetrics(): List<HealthMetric> {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        return dao.getMetricsFrom(oneHourAgo)
    }

    suspend fun clearUploadedMetrics(upToTimestampMs: Long) {
        dao.deleteUpTo(upToTimestampMs)
    }
}
