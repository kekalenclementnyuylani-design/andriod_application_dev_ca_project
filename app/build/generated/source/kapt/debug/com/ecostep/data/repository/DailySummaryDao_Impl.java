package com.ecostep.data.repository;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ecostep.data.model.DailySummary;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DailySummaryDao_Impl implements DailySummaryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailySummary> __insertionAdapterOfDailySummary;

  private final EntityDeletionOrUpdateAdapter<DailySummary> __deletionAdapterOfDailySummary;

  public DailySummaryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailySummary = new EntityInsertionAdapter<DailySummary>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_summaries` (`date`,`totalCo2Grams`,`walkingMeters`,`runningMeters`,`vehicleMeters`,`co2SavedGrams`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailySummary entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
        statement.bindDouble(2, entity.getTotalCo2Grams());
        statement.bindDouble(3, entity.getWalkingMeters());
        statement.bindDouble(4, entity.getRunningMeters());
        statement.bindDouble(5, entity.getVehicleMeters());
        statement.bindDouble(6, entity.getCo2SavedGrams());
      }
    };
    this.__deletionAdapterOfDailySummary = new EntityDeletionOrUpdateAdapter<DailySummary>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `daily_summaries` WHERE `date` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailySummary entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
      }
    };
  }

  @Override
  public Object insert(final DailySummary summary, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailySummary.insert(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final DailySummary summary, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDailySummary.handle(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailySummary>> getLastSevenDays() {
    final String _sql = "SELECT * FROM daily_summaries ORDER BY date DESC LIMIT 7";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_summaries"}, new Callable<List<DailySummary>>() {
      @Override
      @NonNull
      public List<DailySummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalCo2Grams = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCo2Grams");
          final int _cursorIndexOfWalkingMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "walkingMeters");
          final int _cursorIndexOfRunningMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "runningMeters");
          final int _cursorIndexOfVehicleMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleMeters");
          final int _cursorIndexOfCo2SavedGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "co2SavedGrams");
          final List<DailySummary> _result = new ArrayList<DailySummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailySummary _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final double _tmpTotalCo2Grams;
            _tmpTotalCo2Grams = _cursor.getDouble(_cursorIndexOfTotalCo2Grams);
            final double _tmpWalkingMeters;
            _tmpWalkingMeters = _cursor.getDouble(_cursorIndexOfWalkingMeters);
            final double _tmpRunningMeters;
            _tmpRunningMeters = _cursor.getDouble(_cursorIndexOfRunningMeters);
            final double _tmpVehicleMeters;
            _tmpVehicleMeters = _cursor.getDouble(_cursorIndexOfVehicleMeters);
            final double _tmpCo2SavedGrams;
            _tmpCo2SavedGrams = _cursor.getDouble(_cursorIndexOfCo2SavedGrams);
            _item = new DailySummary(_tmpDate,_tmpTotalCo2Grams,_tmpWalkingMeters,_tmpRunningMeters,_tmpVehicleMeters,_tmpCo2SavedGrams);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getForDate(final String date,
      final Continuation<? super DailySummary> $completion) {
    final String _sql = "SELECT * FROM daily_summaries WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailySummary>() {
      @Override
      @Nullable
      public DailySummary call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalCo2Grams = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCo2Grams");
          final int _cursorIndexOfWalkingMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "walkingMeters");
          final int _cursorIndexOfRunningMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "runningMeters");
          final int _cursorIndexOfVehicleMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleMeters");
          final int _cursorIndexOfCo2SavedGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "co2SavedGrams");
          final DailySummary _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final double _tmpTotalCo2Grams;
            _tmpTotalCo2Grams = _cursor.getDouble(_cursorIndexOfTotalCo2Grams);
            final double _tmpWalkingMeters;
            _tmpWalkingMeters = _cursor.getDouble(_cursorIndexOfWalkingMeters);
            final double _tmpRunningMeters;
            _tmpRunningMeters = _cursor.getDouble(_cursorIndexOfRunningMeters);
            final double _tmpVehicleMeters;
            _tmpVehicleMeters = _cursor.getDouble(_cursorIndexOfVehicleMeters);
            final double _tmpCo2SavedGrams;
            _tmpCo2SavedGrams = _cursor.getDouble(_cursorIndexOfCo2SavedGrams);
            _result = new DailySummary(_tmpDate,_tmpTotalCo2Grams,_tmpWalkingMeters,_tmpRunningMeters,_tmpVehicleMeters,_tmpCo2SavedGrams);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailySummary>> getAll() {
    final String _sql = "SELECT * FROM daily_summaries ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_summaries"}, new Callable<List<DailySummary>>() {
      @Override
      @NonNull
      public List<DailySummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalCo2Grams = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCo2Grams");
          final int _cursorIndexOfWalkingMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "walkingMeters");
          final int _cursorIndexOfRunningMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "runningMeters");
          final int _cursorIndexOfVehicleMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleMeters");
          final int _cursorIndexOfCo2SavedGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "co2SavedGrams");
          final List<DailySummary> _result = new ArrayList<DailySummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailySummary _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final double _tmpTotalCo2Grams;
            _tmpTotalCo2Grams = _cursor.getDouble(_cursorIndexOfTotalCo2Grams);
            final double _tmpWalkingMeters;
            _tmpWalkingMeters = _cursor.getDouble(_cursorIndexOfWalkingMeters);
            final double _tmpRunningMeters;
            _tmpRunningMeters = _cursor.getDouble(_cursorIndexOfRunningMeters);
            final double _tmpVehicleMeters;
            _tmpVehicleMeters = _cursor.getDouble(_cursorIndexOfVehicleMeters);
            final double _tmpCo2SavedGrams;
            _tmpCo2SavedGrams = _cursor.getDouble(_cursorIndexOfCo2SavedGrams);
            _item = new DailySummary(_tmpDate,_tmpTotalCo2Grams,_tmpWalkingMeters,_tmpRunningMeters,_tmpVehicleMeters,_tmpCo2SavedGrams);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
