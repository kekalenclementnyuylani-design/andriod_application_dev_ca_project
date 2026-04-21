package com.ecostep.data.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\u0006j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b\u00a8\u0006\f"}, d2 = {"Lcom/ecostep/data/model/MovementMode;", "", "(Ljava/lang/String;I)V", "getCarbonPerKm", "", "getEmoji", "", "getLabel", "STATIONARY", "WALKING", "RUNNING", "VEHICLE", "app_debug"})
public enum MovementMode {
    /*public static final*/ STATIONARY /* = new STATIONARY() */,
    /*public static final*/ WALKING /* = new WALKING() */,
    /*public static final*/ RUNNING /* = new RUNNING() */,
    /*public static final*/ VEHICLE /* = new VEHICLE() */;
    
    MovementMode() {
    }
    
    public final double getCarbonPerKm() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmoji() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.ecostep.data.model.MovementMode> getEntries() {
        return null;
    }
}