package com.example.healthmonitor.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HealthMetricDao {
    @Insert
    suspend fun insert(metric: HealthMetric)

    @Query("SELECT * FROM health_metrics WHERE timestampMs >= :fromTimestamp")
    suspend fun getMetricsFrom(fromTimestamp: Long): List<HealthMetric>

    @Query("DELETE FROM health_metrics WHERE timestampMs <= :toTimestamp")
    suspend fun deleteUpTo(toTimestamp: Long)
}
