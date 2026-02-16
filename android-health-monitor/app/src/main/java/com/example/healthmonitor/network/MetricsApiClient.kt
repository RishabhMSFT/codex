package com.example.healthmonitor.network

import com.example.healthmonitor.data.HealthMetric
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class MetricsApiClient(
    private val baseUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) {
    fun uploadMetrics(metrics: List<HealthMetric>): Boolean {
        if (metrics.isEmpty()) {
            return true
        }

        val payload = JSONObject().apply {
            put("collectedAt", System.currentTimeMillis())
            put("metrics", JSONArray().apply {
                metrics.forEach { metric ->
                    put(
                        JSONObject().apply {
                            put("timestampMs", metric.timestampMs)
                            put("appUsedMemoryMb", metric.appUsedMemoryMb)
                            put("appMaxMemoryMb", metric.appMaxMemoryMb)
                            put("systemAvailMemoryMb", metric.systemAvailMemoryMb)
                            put("systemTotalMemoryMb", metric.systemTotalMemoryMb)
                            put("lowMemory", metric.lowMemory)
                        }
                    )
                }
            })
        }

        val request = Request.Builder()
            .url(baseUrl)
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            return response.isSuccessful
        }
    }
}
