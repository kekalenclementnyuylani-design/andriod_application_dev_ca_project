package com.ecostep.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Movement modes detected from sensors
enum class MovementMode {
    STATIONARY, WALKING, RUNNING, VEHICLE;

    fun getCarbonPerKm(): Double = when (this) {
        STATIONARY -> 0.0
        WALKING    -> 0.0
        RUNNING    -> 0.0
        VEHICLE    -> 120.0  // grams CO2 per km (average petrol car)
    }

    fun getLabel(): String = when (this) {
        STATIONARY -> "Stationary"
        WALKING    -> "Walking"
        RUNNING    -> "Running"
        VEHICLE    -> "In vehicle"
    }

    fun getEmoji(): String = when (this) {
        STATIONARY -> "🧍"
        WALKING    -> "🚶"
        RUNNING    -> "🏃"
        VEHICLE    -> "🚗"
    }
}

// A single tracked trip segment
@Entity(tableName = "trip_segments")
data class TripSegment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startTime: Long,
    val endTime: Long = 0L,
    val mode: String,                   // MovementMode name
    val distanceMeters: Double = 0.0,
    val co2Grams: Double = 0.0,
    val date: String = ""               // YYYY-MM-DD for daily grouping
)

// Daily summary stored in DB
@Entity(tableName = "daily_summaries")
data class DailySummary(
    @PrimaryKey val date: String,       // YYYY-MM-DD
    val totalCo2Grams: Double = 0.0,
    val walkingMeters: Double = 0.0,
    val runningMeters: Double = 0.0,
    val vehicleMeters: Double = 0.0,
    val co2SavedGrams: Double = 0.0     // CO2 that would've been emitted if walked = vehicle
)

// Live sensor state passed around the app
data class LiveState(
    val mode: MovementMode = MovementMode.STATIONARY,
    val speedKmh: Float = 0f,
    val co2TodayGrams: Double = 0.0,
    val co2ThisTripGrams: Double = 0.0,
    val distanceTodayMeters: Double = 0.0,
    val stepCount: Int = 0
)
