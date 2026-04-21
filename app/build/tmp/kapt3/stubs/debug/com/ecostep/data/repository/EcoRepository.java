package com.ecostep.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u001a\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010 \u001a\u00020!J\u0018\u0010\"\u001a\u0004\u0018\u00010\f2\u0006\u0010 \u001a\u00020!H\u0086@\u00a2\u0006\u0002\u0010#J\u0010\u0010$\u001a\u0004\u0018\u00010\fH\u0086@\u00a2\u0006\u0002\u0010%J\u0016\u0010&\u001a\u00020\'2\u0006\u0010\u001a\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0006\u0010(\u001a\u00020!J\u0016\u0010)\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010*\u001a\u00020\u00192\u0006\u0010 \u001a\u00020!H\u0086@\u00a2\u0006\u0002\u0010#R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\nR\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\nR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/ecostep/data/repository/EcoRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "allSegments", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/ecostep/data/model/TripSegment;", "getAllSegments", "()Lkotlinx/coroutines/flow/Flow;", "allSummaries", "Lcom/ecostep/data/model/DailySummary;", "getAllSummaries", "db", "Lcom/ecostep/data/repository/EcoDatabase;", "lastSevenDays", "getLastSevenDays", "recentSegments", "getRecentSegments", "summaryDao", "Lcom/ecostep/data/repository/DailySummaryDao;", "tripDao", "Lcom/ecostep/data/repository/TripSegmentDao;", "deleteSegment", "", "segment", "(Lcom/ecostep/data/model/TripSegment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteSummary", "summary", "(Lcom/ecostep/data/model/DailySummary;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSegmentsForDate", "date", "", "getSummaryForDate", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodaySummary", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSegment", "", "todayDate", "updateSegment", "upsertDailySummary", "app_debug"})
public final class EcoRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.ecostep.data.repository.EcoDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ecostep.data.repository.TripSegmentDao tripDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ecostep.data.repository.DailySummaryDao summaryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.TripSegment>> recentSegments = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.TripSegment>> allSegments = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.DailySummary>> lastSevenDays = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.DailySummary>> allSummaries = null;
    
    public EcoRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.TripSegment>> getRecentSegments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.TripSegment>> getAllSegments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.DailySummary>> getLastSevenDays() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.DailySummary>> getAllSummaries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.ecostep.data.model.TripSegment>> getSegmentsForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSummaryForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.ecostep.data.model.DailySummary> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveSegment(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.TripSegment segment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateSegment(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.TripSegment segment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteSegment(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.TripSegment segment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteSummary(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.DailySummary summary, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object upsertDailySummary(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTodaySummary(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.ecostep.data.model.DailySummary> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String todayDate() {
        return null;
    }
}