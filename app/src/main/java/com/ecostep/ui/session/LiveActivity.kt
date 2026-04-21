package com.ecostep.ui.session

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ecostep.databinding.ActivityLiveBinding
import com.ecostep.service.TrackingService
import com.ecostep.ui.report.DailyReportActivity
import com.ecostep.util.TrackingManager

class LiveActivity : AppCompatActivity() {

    private lateinit var b: ActivityLiveBinding
    private val handler = Handler(Looper.getMainLooper())
    private val ticker = object : Runnable {
        override fun run() { refreshUI(); handler.postDelayed(this, 1000) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLiveBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnStopTracking.setOnClickListener {
            startService(Intent(this, TrackingService::class.java).apply {
                action = TrackingService.ACTION_STOP
            })
            // Open report after stopping
            startActivity(Intent(this, DailyReportActivity::class.java))
            finish()
        }

        b.btnBack.setOnClickListener { finish() }
        handler.post(ticker)
    }

    private fun refreshUI() {
        val mode  = TrackingManager.currentMode
        val speed = TrackingManager.currentSpeedKmh
        val co2Today = TrackingManager.co2TodayGrams
        val co2Trip  = TrackingManager.currentTripCo2Grams

        b.tvCurrentMode.text  = mode.getLabel()
        b.tvSpeed.text        = "${"%.1f".format(speed)} km/h"
        b.tvCo2Trip.text      = "${"%.1f".format(co2Trip)} g"
        b.tvCo2Today.text     = "${"%.2f".format(co2Today / 1000)} kg CO₂ today"

        // Color the mode card based on emission level
        val cardColor = when (mode) {
            com.ecostep.data.model.MovementMode.WALKING,
            com.ecostep.data.model.MovementMode.RUNNING    -> 0xFF27500A.toInt()
            com.ecostep.data.model.MovementMode.VEHICLE    -> 0xFF791F1F.toInt()
            com.ecostep.data.model.MovementMode.STATIONARY -> 0xFF444441.toInt()
        }
        b.cardMode.setCardBackgroundColor(cardColor)

        // Live CO2 rate label
        b.tvCo2Rate.text = when (mode) {
            com.ecostep.data.model.MovementMode.VEHICLE    -> "⚠ Emitting CO₂ now"
            com.ecostep.data.model.MovementMode.WALKING,
            com.ecostep.data.model.MovementMode.RUNNING    -> "Zero emissions"
            com.ecostep.data.model.MovementMode.STATIONARY -> "No movement"
        }

        // Distance breakdown
        b.tvWalkKm.text   = "${"%.2f".format(TrackingManager.walkingMetersToday / 1000)} km walking"
        b.tvVehicleKm.text = "${"%.2f".format(TrackingManager.vehicleMetersToday / 1000)} km vehicle"
        b.tvSaved.text    = "${TrackingManager.getSavedKg()} kg CO₂ saved by walking"
    }

    override fun onDestroy() {
        handler.removeCallbacks(ticker)
        super.onDestroy()
    }
}
