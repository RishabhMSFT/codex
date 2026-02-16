package com.example.healthmonitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMs: Long,
    val appUsedMemoryMb: Long,
    val appMaxMemoryMb: Long,
    val systemAvailMemoryMb: Long,
    val systemTotalMemoryMb: Long,
    val lowMemory: Boolean
)
