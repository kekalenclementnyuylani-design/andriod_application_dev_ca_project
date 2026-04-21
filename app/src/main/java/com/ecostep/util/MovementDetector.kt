package com.ecostep.util

import com.ecostep.data.model.MovementMode
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Classifies movement mode from raw accelerometer + GPS speed data.
 *
 * Detection logic:
 *  - STATIONARY : very low accel variance + speed < 2 km/h
 *  - WALKING    : rhythmic accel peaks 1-3 Hz + speed < 8 km/h
 *  - RUNNING    : rhythmic accel peaks 2-4 Hz + speed < 20 km/h
 *  - VEHICLE    : smooth continuous accel + speed > 15 km/h
 */
class MovementDetector {

    private val accelWindow = ArrayDeque<Float>(30)  // sliding window of magnitude readings
    private var lastMode = MovementMode.STATIONARY
    private var modeConfidence = 0                   // consecutive confirmations before switching
    private var pendingMode = MovementMode.STATIONARY

    companion object {
        private const val WINDOW_SIZE = 20
        private const val CONFIDENCE_THRESHOLD = 3   // need 3 consistent readings to switch mode

        // Thresholds
        private const val VARIANCE_STATIONARY = 0.15f
        private const val VARIANCE_WALKING_MIN = 0.5f
        private const val VARIANCE_WALKING_MAX = 4.0f
        private const val VARIANCE_RUNNING_MIN = 4.0f
        private const val SPEED_STATIONARY_MAX = 2f    // km/h
        private const val SPEED_WALKING_MAX = 9f
        private const val SPEED_RUNNING_MAX = 22f
        private const val SPEED_VEHICLE_MIN = 12f
    }

    /**
     * Feed a new accelerometer reading and current GPS speed.
     * Returns the current best-guess MovementMode.
     */
    fun update(ax: Float, ay: Float, az: Float, speedKmh: Float): MovementMode {
        // Calculate magnitude minus gravity
        val magnitude = sqrt(ax * ax + ay * ay + az * az) - 9.8f
        val absMag = abs(magnitude)

        // Maintain sliding window
        if (accelWindow.size >= WINDOW_SIZE) accelWindow.removeFirst()
        accelWindow.addLast(absMag)

        if (accelWindow.size < 5) return lastMode  // not enough data yet

        val variance = calculateVariance(accelWindow)
        val detected = classify(variance, speedKmh)

        // Smoothing: only switch mode after CONFIDENCE_THRESHOLD consistent readings
        if (detected == pendingMode) {
            modeConfidence++
            if (modeConfidence >= CONFIDENCE_THRESHOLD) {
                lastMode = detected
            }
        } else {
            pendingMode = detected
            modeConfidence = 1
        }

        return lastMode
    }

    private fun classify(variance: Float, speedKmh: Float): MovementMode {
        return when {
            // Speed-based overrides first (GPS is very reliable)
            speedKmh > SPEED_VEHICLE_MIN && variance < VARIANCE_WALKING_MIN -> MovementMode.VEHICLE
            speedKmh > SPEED_VEHICLE_MIN -> MovementMode.VEHICLE

            // Then accel variance based
            variance < VARIANCE_STATIONARY && speedKmh < SPEED_STATIONARY_MAX -> MovementMode.STATIONARY
            variance in VARIANCE_WALKING_MIN..VARIANCE_WALKING_MAX && speedKmh < SPEED_WALKING_MAX -> MovementMode.WALKING
            variance >= VARIANCE_RUNNING_MIN && speedKmh < SPEED_RUNNING_MAX -> MovementMode.RUNNING

            // Fallback
            speedKmh < SPEED_STATIONARY_MAX -> MovementMode.STATIONARY
            else -> MovementMode.WALKING
        }
    }

    private fun calculateVariance(data: Collection<Float>): Float {
        if (data.isEmpty()) return 0f
        val mean = data.average().toFloat()
        return data.map { (it - mean) * (it - mean) }.average().toFloat()
    }

    fun reset() {
        accelWindow.clear()
        lastMode = MovementMode.STATIONARY
        modeConfidence = 0
        pendingMode = MovementMode.STATIONARY
    }
}
