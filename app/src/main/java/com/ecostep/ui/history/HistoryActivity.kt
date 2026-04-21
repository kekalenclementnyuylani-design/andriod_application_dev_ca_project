package com.ecostep.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecostep.R
import com.ecostep.data.model.DailySummary
import com.ecostep.data.repository.EcoRepository
import com.ecostep.databinding.ActivityHistoryBinding
import com.ecostep.ui.report.DailyReportActivity
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var b: ActivityHistoryBinding
    private lateinit var repo: EcoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(b.root)
        repo = EcoRepository(this)

        b.btnBack.setOnClickListener { finish() }

        lifecycleScope.launch {
            repo.allSummaries.collect { summaries ->
                runOnUiThread { displaySummaries(summaries) }
            }
        }
    }

    private fun displaySummaries(summaries: List<DailySummary>) {
        if (summaries.isEmpty()) {
            b.rvDays.visibility = View.GONE
            b.tvTotalWeekCo2.text = "0.00 kg"
            b.tvTotalSaved.text = "0.00 kg saved"
            b.tvCleanestDay.text = "—"
            b.tvWorstDay.text = "—"
            return
        }
        b.rvDays.visibility = View.VISIBLE

        val totalCo2 = summaries.sumOf { it.totalCo2Grams }
        val totalSaved = summaries.sumOf { it.co2SavedGrams }
        val cleanestDay = summaries.minByOrNull { it.totalCo2Grams }
        val worstDay = summaries.maxByOrNull { it.totalCo2Grams }

        b.tvTotalWeekCo2.text = "${"%.2f".format(totalCo2 / 1000)} kg"
        b.tvTotalSaved.text = "${"%.2f".format(totalSaved / 1000)} kg saved"
        b.tvCleanestDay.text = cleanestDay?.date ?: "—"
        b.tvWorstDay.text = worstDay?.date ?: "—"

        b.rvDays.layoutManager = LinearLayoutManager(this)
        b.rvDays.adapter = HistoryAdapter(summaries, 
            onItemClick = { summary ->
                val intent = Intent(this, DailyReportActivity::class.java)
                intent.putExtra("date", summary.date)
                startActivity(intent)
            },
            onItemLongClick = { summary ->
                confirmDelete(summary)
            }
        )
    }

    private fun confirmDelete(summary: DailySummary) {
        AlertDialog.Builder(this)
            .setTitle("Delete entry")
            .setMessage("Are you sure you want to delete the tracking for ${summary.date}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    repo.deleteSummary(summary)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    class HistoryAdapter(
        private val items: List<DailySummary>,
        private val onItemClick: (DailySummary) -> Unit,
        private val onItemLongClick: (DailySummary) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.VH>() {

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val tvDate: TextView    = v.findViewById(R.id.tv_date)
            val tvCo2: TextView     = v.findViewById(R.id.tv_co2)
            val tvWalk: TextView    = v.findViewById(R.id.tv_walk)
            val tvVehicle: TextView = v.findViewById(R.id.tv_vehicle)
            val tvSaved: TextView   = v.findViewById(R.id.tv_saved)
        }

        override fun onCreateViewHolder(p: ViewGroup, t: Int) =
            VH(LayoutInflater.from(p.context).inflate(R.layout.item_history_day, p, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(h: VH, pos: Int) {
            val item = items[pos]
            h.tvDate.text    = item.date
            h.tvCo2.text     = "${"%.2f".format(item.totalCo2Grams / 1000)} kg CO₂"
            h.tvWalk.text    = "${"%.1f".format(item.walkingMeters / 1000)} km walked"
            h.tvVehicle.text = "${"%.1f".format(item.vehicleMeters / 1000)} km vehicle"
            h.tvSaved.text   = "${"%.2f".format(item.co2SavedGrams / 1000)} kg saved"

            // Color code by CO2 level
            val bg = when {
                item.totalCo2Grams < 200  -> 0xFFEAF3DE.toInt()
                item.totalCo2Grams < 1000 -> 0xFFFAEEDA.toInt()
                else                      -> 0xFFFCEBEB.toInt()
            }
            h.itemView.setBackgroundColor(bg)

            h.itemView.setOnClickListener { onItemClick(item) }
            h.itemView.setOnLongClickListener { 
                onItemLongClick(item)
                true
            }
        }
    }
}
