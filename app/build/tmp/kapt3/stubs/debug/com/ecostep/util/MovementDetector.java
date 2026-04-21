package com.ecostep.util;

/**
 * Classifies movement mode from raw accelerometer + GPS speed data.
 *
 * Detection logic:
 * - STATIONARY : very low accel variance + speed < 2 km/h
 * - WALKING    : rhythmic accel peaks 1-3 Hz + speed < 8 km/h
 * - RUNNING    : rhythmic accel peaks 2-4 Hz + speed < 20 km/h
 * - VEHICLE    : smooth continuous accel + speed > 15 km/h
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000b\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\rH\u0002J\u0018\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005H\u0002J\u0006\u0010\u0011\u001a\u00020\u0012J&\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/ecostep/util/MovementDetector;", "", "()V", "accelWindow", "Lkotlin/collections/ArrayDeque;", "", "lastMode", "Lcom/ecostep/data/model/MovementMode;", "modeConfidence", "", "pendingMode", "calculateVariance", "data", "", "classify", "variance", "speedKmh", "reset", "", "update", "ax", "ay", "az", "Companion", "app_debug"})
public final class MovementDetector {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.collections.ArrayDeque<java.lang.Float> accelWindow = null;
    @org.jetbrains.annotations.NotNull()
    private com.ecostep.data.model.MovementMode lastMode = com.ecostep.data.model.MovementMode.STATIONARY;
    private int modeConfidence = 0;
    @org.jetbrains.annotations.NotNull()
    private com.ecostep.data.model.MovementMode pendingMode = com.ecostep.data.model.MovementMode.STATIONARY;
    private static final int WINDOW_SIZE = 20;
    private static final int CONFIDENCE_THRESHOLD = 3;
    private static final float VARIANCE_STATIONARY = 0.15F;
    private static final float VARIANCE_WALKING_MIN = 0.5F;
    private static final float VARIANCE_WALKING_MAX = 4.0F;
    private static final float VARIANCE_RUNNING_MIN = 4.0F;
    private static final float SPEED_STATIONARY_MAX = 2.0F;
    private static final float SPEED_WALKING_MAX = 9.0F;
    private static final float SPEED_RUNNING_MAX = 22.0F;
    private static final float SPEED_VEHICLE_MIN = 12.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.ecostep.util.MovementDetector.Companion Companion = null;
    
    public MovementDetector() {
        super();
    }
    
    /**
     * Feed a new accelerometer reading and current GPS speed.
     * Returns the current best-guess MovementMode.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.ecostep.data.model.MovementMode update(float ax, float ay, float az, float speedKmh) {
        return null;
    }
    
    private final com.ecostep.data.model.MovementMode classify(float variance, float speedKmh) {
        return null;
    }
    
    private final float calculateVariance(java.util.Collection<java.lang.Float> data) {
        return 0.0F;
    }
    
    public final void reset() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ecostep/util/MovementDetector$Companion;", "", "()V", "CONFIDENCE_THRESHOLD", "", "SPEED_RUNNING_MAX", "", "SPEED_STATIONARY_MAX", "SPEED_VEHICLE_MIN", "SPEED_WALKING_MAX", "VARIANCE_RUNNING_MIN", "VARIANCE_STATIONARY", "VARIANCE_WALKING_MAX", "VARIANCE_WALKING_MIN", "WINDOW_SIZE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}