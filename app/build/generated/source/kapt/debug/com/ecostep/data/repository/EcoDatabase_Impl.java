package com.ecostep.data.repository;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EcoDatabase_Impl extends EcoDatabase {
  private volatile TripSegmentDao _tripSegmentDao;

  private volatile DailySummaryDao _dailySummaryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `trip_segments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `mode` TEXT NOT NULL, `distanceMeters` REAL NOT NULL, `co2Grams` REAL NOT NULL, `date` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_summaries` (`date` TEXT NOT NULL, `totalCo2Grams` REAL NOT NULL, `walkingMeters` REAL NOT NULL, `runningMeters` REAL NOT NULL, `vehicleMeters` REAL NOT NULL, `co2SavedGrams` REAL NOT NULL, PRIMARY KEY(`date`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd0d5f2ee9a7db590b368b95e5c84c9e4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `trip_segments`");
        db.execSQL("DROP TABLE IF EXISTS `daily_summaries`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTripSegments = new HashMap<String, TableInfo.Column>(7);
        _columnsTripSegments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("mode", new TableInfo.Column("mode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("distanceMeters", new TableInfo.Column("distanceMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("co2Grams", new TableInfo.Column("co2Grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTripSegments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTripSegments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTripSegments = new TableInfo("trip_segments", _columnsTripSegments, _foreignKeysTripSegments, _indicesTripSegments);
        final TableInfo _existingTripSegments = TableInfo.read(db, "trip_segments");
        if (!_infoTripSegments.equals(_existingTripSegments)) {
          return new RoomOpenHelper.ValidationResult(false, "trip_segments(com.ecostep.data.model.TripSegment).\n"
                  + " Expected:\n" + _infoTripSegments + "\n"
                  + " Found:\n" + _existingTripSegments);
        }
        final HashMap<String, TableInfo.Column> _columnsDailySummaries = new HashMap<String, TableInfo.Column>(6);
        _columnsDailySummaries.put("date", new TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailySummaries.put("totalCo2Grams", new TableInfo.Column("totalCo2Grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailySummaries.put("walkingMeters", new TableInfo.Column("walkingMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailySummaries.put("runningMeters", new TableInfo.Column("runningMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailySummaries.put("vehicleMeters", new TableInfo.Column("vehicleMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailySummaries.put("co2SavedGrams", new TableInfo.Column("co2SavedGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailySummaries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailySummaries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailySummaries = new TableInfo("daily_summaries", _columnsDailySummaries, _foreignKeysDailySummaries, _indicesDailySummaries);
        final TableInfo _existingDailySummaries = TableInfo.read(db, "daily_summaries");
        if (!_infoDailySummaries.equals(_existingDailySummaries)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_summaries(com.ecostep.data.model.DailySummary).\n"
                  + " Expected:\n" + _infoDailySummaries + "\n"
                  + " Found:\n" + _existingDailySummaries);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d0d5f2ee9a7db590b368b95e5c84c9e4", "04c74eec5e1c29ce83470a8c453f1692");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "trip_segments","daily_summaries");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `trip_segments`");
      _db.execSQL("DELETE FROM `daily_summaries`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TripSegmentDao.class, TripSegmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DailySummaryDao.class, DailySummaryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TripSegmentDao tripSegmentDao() {
    if (_tripSegmentDao != null) {
      return _tripSegmentDao;
    } else {
      synchronized(this) {
        if(_tripSegmentDao == null) {
          _tripSegmentDao = new TripSegmentDao_Impl(this);
        }
        return _tripSegmentDao;
      }
    }
  }

  @Override
  public DailySummaryDao dailySummaryDao() {
    if (_dailySummaryDao != null) {
      return _dailySummaryDao;
    } else {
      synchronized(this) {
        if(_dailySummaryDao == null) {
          _dailySummaryDao = new DailySummaryDao_Impl(this);
        }
        return _dailySummaryDao;
      }
    }
  }
}
