package com.ecostep.util

import com.ecostep.data.model.LiveState
import com.ecostep.data.model.MovementMode

object TrackingManager {

    var isTracking = false
    var currentMode = MovementMode.STATIONARY
    var currentSpeedKmh = 0f
    var currentLat = 0.0
    var currentLng = 0.0

    // Today's totals
    var co2TodayGrams = 0.0
    var walkingMetersToday = 0.0
    var runningMetersToday = 0.0
    var vehicleMetersToday = 0.0
    var co2SavedGrams = 0.0

    // Current trip
    var currentTripCo2Grams = 0.0
    var currentTripDistanceMeters = 0.0
    var currentTripStartTime = 0L
    var stepCount = 0

    fun getLiveState() = LiveState(
        mode = currentMode,
        speedKmh = currentSpeedKmh,
        co2TodayGrams = co2TodayGrams,
        co2ThisTripGrams = currentTripCo2Grams,
        distanceTodayMeters = walkingMetersToday + runningMetersToday + vehicleMetersToday,
        stepCount = stepCount
    )

    fun addDistance(meters: Double, mode: MovementMode) {
        val km = meters / 1000.0
        val co2 = km * mode.getCarbonPerKm()
        co2TodayGrams += co2
        currentTripCo2Grams += co2
        currentTripDistanceMeters += meters

        when (mode) {
            MovementMode.WALKING -> {
                walkingMetersToday += meters
                co2SavedGrams += km * 120.0  // saved vs driving
            }
            MovementMode.RUNNING -> {
                runningMetersToday += meters
                co2SavedGrams += km * 120.0
            }
            MovementMode.VEHICLE -> vehicleMetersToday += meters
            MovementMode.STATIONARY -> {}
        }
    }

    fun startNewTrip() {
        currentTripCo2Grams = 0.0
        currentTripDistanceMeters = 0.0
        currentTripStartTime = System.currentTimeMillis()
    }

    fun resetDay() {
        co2TodayGrams = 0.0
        walkingMetersToday = 0.0
        runningMetersToday = 0.0
        vehicleMetersToday = 0.0
        co2SavedGrams = 0.0
        stepCount = 0
        startNewTrip()
    }

    fun getCo2Kg(): String = "%.2f".format(co2TodayGrams / 1000.0)
    fun getSavedKg(): String = "%.2f".format(co2SavedGrams / 1000.0)

    fun getCo2Equivalent(): String {
        val grams = co2TodayGrams
        return when {
            grams < 100  -> "Less than charging your phone once"
            grams < 500  -> "Like charging your phone ${(grams / 12).toInt()} times"
            grams < 2000 -> "Like streaming video for ${(grams / 36).toInt()} hours"
            else         -> "Like driving ${(grams / 120).toInt()} km"
        }
    }
}
