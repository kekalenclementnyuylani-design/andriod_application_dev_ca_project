package com.ecostep.data.repository

import android.content.Context
import androidx.room.*
import com.ecostep.data.model.DailySummary
import com.ecostep.data.model.TripSegment
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

// ── DAOs ──────────────────────────────────────────────────────────────────────

@Dao
interface TripSegmentDao {
    @Insert
    suspend fun insert(segment: TripSegment): Long

    @Update
    suspend fun update(segment: TripSegment)

    @Delete
    suspend fun delete(segment: TripSegment)

    @Query("DELETE FROM trip_segments WHERE date = :date")
    suspend fun deleteForDate(date: String)

    @Query("SELECT * FROM trip_segments WHERE date = :date ORDER BY startTime ASC")
    fun getSegmentsForDate(date: String): Flow<List<TripSegment>>

    @Query("SELECT * FROM trip_segments ORDER BY startTime DESC")
    fun getAllSegments(): Flow<List<TripSegment>>

    @Query("SELECT * FROM trip_segments ORDER BY startTime DESC LIMIT 50")
    fun getRecentSegments(): Flow<List<TripSegment>>

    @Query("SELECT SUM(co2Grams) FROM trip_segments WHERE date = :date")
    suspend fun getTotalCo2ForDate(date: String): Double?

    @Query("SELECT SUM(distanceMeters) FROM trip_segments WHERE date = :date AND mode = :mode")
    suspend fun getDistanceForDateAndMode(date: String, mode: String): Double?
}

@Dao
interface DailySummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(summary: DailySummary)

    @Delete
    suspend fun delete(summary: DailySummary)

    @Query("SELECT * FROM daily_summaries ORDER BY date DESC LIMIT 7")
    fun getLastSevenDays(): Flow<List<DailySummary>>

    @Query("SELECT * FROM daily_summaries WHERE date = :date")
    suspend fun getForDate(date: String): DailySummary?

    @Query("SELECT * FROM daily_summaries ORDER BY date DESC")
    fun getAll(): Flow<List<DailySummary>>
}

// ── Database ──────────────────────────────────────────────────────────────────

@Database(entities = [TripSegment::class, DailySummary::class], version = 1, exportSchema = false)
abstract class EcoDatabase : RoomDatabase() {
    abstract fun tripSegmentDao(): TripSegmentDao
    abstract fun dailySummaryDao(): DailySummaryDao

    companion object {
        @Volatile private var INSTANCE: EcoDatabase? = null
        fun getInstance(context: Context): EcoDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, EcoDatabase::class.java, "eco_db")
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}

// ── Repository ────────────────────────────────────────────────────────────────

class EcoRepository(context: Context) {
    private val db = EcoDatabase.getInstance(context)
    private val tripDao = db.tripSegmentDao()
    private val summaryDao = db.dailySummaryDao()

    val recentSegments = tripDao.getRecentSegments()
    val allSegments    = tripDao.getAllSegments()
    val lastSevenDays  = summaryDao.getLastSevenDays()
    val allSummaries   = summaryDao.getAll()

    fun getSegmentsForDate(date: String) = tripDao.getSegmentsForDate(date)
    suspend fun getSummaryForDate(date: String) = summaryDao.getForDate(date)

    suspend fun saveSegment(segment: TripSegment) = tripDao.insert(segment)
    suspend fun updateSegment(segment: TripSegment) = tripDao.update(segment)
    
    suspend fun deleteSegment(segment: TripSegment) {
        tripDao.delete(segment)
        // Refresh summary for that date
        upsertDailySummary(segment.date)
    }

    suspend fun deleteSummary(summary: DailySummary) {
        summaryDao.delete(summary)
        tripDao.deleteForDate(summary.date)
    }

    suspend fun upsertDailySummary(date: String) {
        val co2    = tripDao.getTotalCo2ForDate(date) ?: 0.0
        val walk   = tripDao.getDistanceForDateAndMode(date, "WALKING")  ?: 0.0
        val run    = tripDao.getDistanceForDateAndMode(date, "RUNNING")  ?: 0.0
        val veh    = tripDao.getDistanceForDateAndMode(date, "VEHICLE")  ?: 0.0
        val saved  = (walk + run) * 0.12    // CO2 saved vs driving same distance
        
        if (co2 == 0.0 && walk == 0.0 && run == 0.0 && veh == 0.0) {
            summaryDao.getForDate(date)?.let { summaryDao.delete(it) }
        } else {
            summaryDao.insert(DailySummary(date, co2, walk, run, veh, saved))
        }
    }

    suspend fun getTodaySummary(): DailySummary? =
        summaryDao.getForDate(todayDate())

    fun todayDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
