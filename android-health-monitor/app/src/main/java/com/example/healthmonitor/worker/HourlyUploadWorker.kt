package com.example.healthmonitor.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.healthmonitor.data.HealthMetricsRepository
import com.example.healthmonitor.network.MetricsApiClient

class HourlyUploadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val repository = HealthMetricsRepository(appContext)

    override suspend fun doWork(): Result {
        val serverUrl = inputData.getString(KEY_SERVER_URL) ?: return Result.failure()
        val metrics = repository.getLastHourMetrics()
        val success = MetricsApiClient(serverUrl).uploadMetrics(metrics)

        return if (success) {
            repository.clearUploadedMetrics(System.currentTimeMillis())
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val KEY_SERVER_URL = "key_server_url"
        const val UNIQUE_WORK_NAME = "hourly_upload_work"
    }
}
