package com.ecostep.ui.home

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ecostep.databinding.ActivityMainBinding
import com.ecostep.service.TrackingService
import com.ecostep.ui.history.HistoryActivity
import com.ecostep.ui.session.LiveActivity
import com.ecostep.util.TrackingManager

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val ticker = object : Runnable {
        override fun run() { refreshUI(); handler.postDelayed(this, 2000) }
    }

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            runOnUiThread { refreshUI() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        requestPermissions()

        b.btnStartTracking.setOnClickListener {
            if (!TrackingManager.isTracking) {
                startService(Intent(this, TrackingService::class.java))
            }
            startActivity(Intent(this, LiveActivity::class.java))
        }

        b.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        handler.post(ticker)
        registerReceiver(updateReceiver, IntentFilter("com.ecostep.LOCATION_UPDATE"), RECEIVER_NOT_EXPORTED)
    }

    private fun refreshUI() {
        b.tvCo2Today.text = "${TrackingManager.getCo2Kg()} kg"
        b.tvCo2Saved.text = "${TrackingManager.getSavedKg()} kg saved"
        b.tvCurrentMode.text = TrackingManager.currentMode.getLabel()
        b.tvEquivalent.text = TrackingManager.getCo2Equivalent()

        val walkKm  = "%.1f".format(TrackingManager.walkingMetersToday / 1000)
        val vehKm   = "%.1f".format(TrackingManager.vehicleMetersToday / 1000)
        b.tvWalkDistance.text = "${walkKm} km"
        b.tvVehicleDistance.text = "${vehKm} km"

        b.btnStartTracking.text = if (TrackingManager.isTracking) "Live view" else "Start tracking"
        b.tvTrackingStatus.text = if (TrackingManager.isTracking) "Tracking active" else "Not tracking"
        b.tvTrackingStatus.setTextColor(
            if (TrackingManager.isTracking) 0xFF27500A.toInt() else 0xFF888780.toInt()
        )
    }

    private fun requestPermissions() {
        val perms = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) perms.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) perms.add(Manifest.permission.ACTIVITY_RECOGNITION)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) perms.add(Manifest.permission.POST_NOTIFICATIONS)
        if (perms.isNotEmpty()) ActivityCompat.requestPermissions(this, perms.toTypedArray(), 1001)
    }

    override fun onDestroy() {
        handler.removeCallbacks(ticker)
        try { unregisterReceiver(updateReceiver) } catch (_: Exception) {}
        super.onDestroy()
    }
}
