package com.ecostep.service

import android.app.*
import android.content.*
import android.hardware.*
import android.location.*
import android.os.*
import androidx.core.app.NotificationCompat
import com.ecostep.R
import com.ecostep.data.model.MovementMode
import com.ecostep.data.model.TripSegment
import com.ecostep.data.repository.EcoRepository
import com.ecostep.ui.home.MainActivity
import com.ecostep.util.MovementDetector
import com.ecostep.util.TrackingManager
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class TrackingService : Service(), SensorEventListener, LocationListener {

    companion object {
        const val CHANNEL_ID = "ecostep_channel"
        const val NOTIF_ID = 1
        const val ACTION_STOP = "com.ecostep.STOP"
        private const val SEGMENT_SAVE_INTERVAL_MS = 30_000L  // save segment every 30s
    }

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var locationManager: LocationManager
    private val detector = MovementDetector()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var repository: EcoRepository

    private var lastLat = 0.0
    private var lastLng = 0.0
    private var lastLocationTime = 0L
    private var currentSpeedKmh = 0f
    private var currentSegmentMode = MovementMode.STATIONARY
    private var segmentStartTime = System.currentTimeMillis()

    override fun onCreate() {
        super.onCreate()
        repository = EcoRepository(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) { stopTracking(); return START_NOT_STICKY }
        startTracking()
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        try { locationManager.removeUpdates(this) } catch (_: Exception) {}
        scope.cancel()
        super.onDestroy()
    }

    private fun startTracking() {
        TrackingManager.isTracking = true
        TrackingManager.startNewTrip()
        startForeground(NOTIF_ID, buildNotif())

        // Register accelerometer
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Request GPS updates
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 3000L, 5f, this
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000L, 10f, this
            )
        } catch (e: SecurityException) {
            // Permission not granted — GPS won't work but accelerometer still will
        }

        // Periodic segment saver
        scope.launch {
            while (TrackingManager.isTracking) {
                delay(SEGMENT_SAVE_INTERVAL_MS)
                saveCurrentSegment()
                updateNotif()
            }
        }
    }

    private fun stopTracking() {
        TrackingManager.isTracking = false
        sensorManager.unregisterListener(this)
        try { locationManager.removeUpdates(this) } catch (_: Exception) {}
        scope.launch {
            saveCurrentSegment()
            repository.upsertDailySummary(todayDate())
        }
        stopSelf()
    }

    // ── Accelerometer ─────────────────────────────────────────────────────────

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val newMode = detector.update(
            event.values[0], event.values[1], event.values[2], currentSpeedKmh
        )
        TrackingManager.currentMode = newMode

        // If mode changed, save old segment and start new one
        if (newMode != currentSegmentMode) {
            scope.launch { saveCurrentSegment() }
            currentSegmentMode = newMode
            segmentStartTime = System.currentTimeMillis()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // ── GPS / Location ────────────────────────────────────────────────────────

    override fun onLocationChanged(location: Location) {
        currentSpeedKmh = location.speed * 3.6f  // m/s to km/h
        TrackingManager.currentSpeedKmh = currentSpeedKmh
        TrackingManager.currentLat = location.latitude
        TrackingManager.currentLng = location.longitude

        // Calculate distance from last location
        if (lastLat != 0.0 && lastLng != 0.0) {
            val results = FloatArray(1)
            Location.distanceBetween(lastLat, lastLng, location.latitude, location.longitude, results)
            val distanceMeters = results[0].toDouble()
            if (distanceMeters > 0 && distanceMeters < 500) {  // ignore GPS jumps
                TrackingManager.addDistance(distanceMeters, TrackingManager.currentMode)
            }
        }

        lastLat = location.latitude
        lastLng = location.longitude
        lastLocationTime = System.currentTimeMillis()

        // Broadcast update to UI
        sendBroadcast(Intent("com.ecostep.LOCATION_UPDATE").apply {
            putExtra("speed", currentSpeedKmh)
            putExtra("co2_today", TrackingManager.co2TodayGrams)
            putExtra("mode", TrackingManager.currentMode.name)
        })
    }

    // ── Segment saving ────────────────────────────────────────────────────────

    private suspend fun saveCurrentSegment() {
        val now = System.currentTimeMillis()
        if (now - segmentStartTime < 5000) return  // don't save very short segments

        val segment = TripSegment(
            startTime = segmentStartTime,
            endTime = now,
            mode = currentSegmentMode.name,
            distanceMeters = TrackingManager.currentTripDistanceMeters,
            co2Grams = TrackingManager.currentTripCo2Grams,
            date = todayDate()
        )
        repository.saveSegment(segment)
        segmentStartTime = now
    }

    // ── Notification ──────────────────────────────────────────────────────────

    private fun createChannel() {
        val ch = NotificationChannel(CHANNEL_ID, "EcoStep", NotificationManager.IMPORTANCE_LOW)
        ch.description = "EcoStep movement tracking"
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)
    }

    private fun buildNotif(): Notification {
        val pi = PendingIntent.getActivity(this, 0,
            Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val mode = TrackingManager.currentMode.getLabel()
        val co2  = TrackingManager.getCo2Kg()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("EcoStep tracking")
            .setContentText("$mode · $co2 kg CO₂ today")
            .setSmallIcon(R.drawable.ic_leaf)
            .setContentIntent(pi)
            .setOngoing(true).build()
    }

    private fun updateNotif() {
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(NOTIF_ID, buildNotif())
    }

    private fun todayDate() =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
