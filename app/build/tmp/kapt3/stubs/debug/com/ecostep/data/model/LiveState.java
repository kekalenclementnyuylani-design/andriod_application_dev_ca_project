package com.ecostep.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u000bH\u00c6\u0003JE\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\u000bH\u00d6\u0001J\t\u0010\"\u001a\u00020#H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006$"}, d2 = {"Lcom/ecostep/data/model/LiveState;", "", "mode", "Lcom/ecostep/data/model/MovementMode;", "speedKmh", "", "co2TodayGrams", "", "co2ThisTripGrams", "distanceTodayMeters", "stepCount", "", "(Lcom/ecostep/data/model/MovementMode;FDDDI)V", "getCo2ThisTripGrams", "()D", "getCo2TodayGrams", "getDistanceTodayMeters", "getMode", "()Lcom/ecostep/data/model/MovementMode;", "getSpeedKmh", "()F", "getStepCount", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class LiveState {
    @org.jetbrains.annotations.NotNull()
    private final com.ecostep.data.model.MovementMode mode = null;
    private final float speedKmh = 0.0F;
    private final double co2TodayGrams = 0.0;
    private final double co2ThisTripGrams = 0.0;
    private final double distanceTodayMeters = 0.0;
    private final int stepCount = 0;
    
    public LiveState(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.MovementMode mode, float speedKmh, double co2TodayGrams, double co2ThisTripGrams, double distanceTodayMeters, int stepCount) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ecostep.data.model.MovementMode getMode() {
        return null;
    }
    
    public final float getSpeedKmh() {
        return 0.0F;
    }
    
    public final double getCo2TodayGrams() {
        return 0.0;
    }
    
    public final double getCo2ThisTripGrams() {
        return 0.0;
    }
    
    public final double getDistanceTodayMeters() {
        return 0.0;
    }
    
    public final int getStepCount() {
        return 0;
    }
    
    public LiveState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ecostep.data.model.MovementMode component1() {
        return null;
    }
    
    public final float component2() {
        return 0.0F;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ecostep.data.model.LiveState copy(@org.jetbrains.annotations.NotNull()
    com.ecostep.data.model.MovementMode mode, float speedKmh, double co2TodayGrams, double co2ThisTripGrams, double distanceTodayMeters, int stepCount) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}