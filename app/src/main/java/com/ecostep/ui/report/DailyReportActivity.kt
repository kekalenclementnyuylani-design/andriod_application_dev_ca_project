package com.ecostep.ui.report

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ecostep.data.repository.EcoRepository
import com.ecostep.databinding.ActivityDailyReportBinding
import com.ecostep.util.TrackingManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DailyReportActivity : AppCompatActivity() {

    private lateinit var b: ActivityDailyReportBinding
    private lateinit var repo: EcoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDailyReportBinding.inflate(layoutInflater)
        setContentView(b.root)
        repo = EcoRepository(this)

        val date = intent.getStringExtra("date")
        if (date != null) {
            b.tvReportTitle.text = "Report for $date"
            loadHistoricalData(date)
        } else {
            b.tvReportTitle.text = "Tracking complete!"
            loadCurrentData()
        }

        b.btnClose.setOnClickListener { finish() }
    }

    private fun loadCurrentData() {
        val co2 = TrackingManager.co2TodayGrams
        displayData(
            co2Grams = co2,
            savedGrams = TrackingManager.co2SavedGrams,
            walk = TrackingManager.walkingMetersToday,
            run = TrackingManager.runningMetersToday,
            veh = TrackingManager.vehicleMetersToday
        )
        provideFeedback(co2)
    }

    private fun loadHistoricalData(date: String) {
        lifecycleScope.launch {
            val summary = repo.getSummaryForDate(date)
            if (summary != null) {
                displayData(
                    summary.totalCo2Grams,
                    summary.co2SavedGrams,
                    summary.walkingMeters,
                    summary.runningMeters,
                    summary.vehicleMeters
                )
                provideFeedback(summary.totalCo2Grams)
            }
        }
    }

    private fun displayData(co2Grams: Double, savedGrams: Double, walk: Double, run: Double, veh: Double) {
        b.tvTotalCo2.text    = "${"%.2f".format(co2Grams / 1000)} kg CO₂"
        b.tvSaved.text       = "${"%.2f".format(savedGrams / 1000)} kg saved"
        b.tvWalking.text     = "${"%.1f".format(walk / 1000)} km"
        b.tvRunning.text     = "${"%.1f".format(run / 1000)} km"
        b.tvVehicle.text     = "${"%.1f".format(veh / 1000)} km"

        b.tvEquivalent.text = when {
            co2Grams < 100  -> "Less than charging your phone once"
            co2Grams < 500  -> "Like charging your phone ${(co2Grams / 12).toInt()} times"
            co2Grams < 2000 -> "Like streaming video for ${(co2Grams / 36).toInt()} hours"
            else            -> "Like driving ${(co2Grams / 120).toInt()} km"
        }
    }

    private fun provideFeedback(currentCo2: Double) {
        lifecycleScope.launch {
            val all = repo.allSummaries.firstOrNull() ?: emptyList()
            if (all.size < 2) {
                b.tvFeedback.visibility = View.GONE
                return@launch
            }

            val min = all.minOf { it.totalCo2Grams }
            val max = all.maxOf { it.totalCo2Grams }
            val avg = all.map { it.totalCo2Grams }.average()

            b.tvFeedback.visibility = View.VISIBLE
            when {
                currentCo2 <= min -> {
                    b.tvFeedback.text = "🏆 This is your Cleanest Day ever!"
                    b.tvFeedback.setTextColor(0xFF27500A.toInt())
                }
                currentCo2 >= max -> {
                    b.tvFeedback.text = "⚠ This is your Worst Day so far."
                    b.tvFeedback.setTextColor(0xFF791F1F.toInt())
                }
                currentCo2 < avg -> {
                    b.tvFeedback.text = "🌱 Better than your average!"
                    b.tvFeedback.setTextColor(0xFF27500A.toInt())
                }
                else -> {
                    b.tvFeedback.text = "Keep trying to reduce your footprint!"
                    b.tvFeedback.setTextColor(0xFF5F5E5A.toInt())
                }
            }
        }
    }
}
