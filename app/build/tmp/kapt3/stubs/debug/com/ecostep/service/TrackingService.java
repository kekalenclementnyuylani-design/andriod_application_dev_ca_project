package com.ecostep.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 :2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001:B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u001a\u0010\u001f\u001a\u00020\u001e2\b\u0010 \u001a\u0004\u0018\u00010\u00062\u0006\u0010!\u001a\u00020\"H\u0016J\u0014\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010\'\u001a\u00020\u001eH\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\u0010\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020+H\u0016J\u0010\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020.H\u0016J\"\u0010/\u001a\u00020\"2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u00100\u001a\u00020\"2\u0006\u00101\u001a\u00020\"H\u0016J\u000e\u00102\u001a\u00020\u001eH\u0082@\u00a2\u0006\u0002\u00103J\b\u00104\u001a\u00020\u001eH\u0002J\b\u00105\u001a\u00020\u001eH\u0002J\u0010\u00106\u001a\n 8*\u0004\u0018\u00010707H\u0002J\b\u00109\u001a\u00020\u001eH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/ecostep/service/TrackingService;", "Landroid/app/Service;", "Landroid/hardware/SensorEventListener;", "Landroid/location/LocationListener;", "()V", "accelerometer", "Landroid/hardware/Sensor;", "currentSegmentMode", "Lcom/ecostep/data/model/MovementMode;", "currentSpeedKmh", "", "detector", "Lcom/ecostep/util/MovementDetector;", "lastLat", "", "lastLng", "lastLocationTime", "", "locationManager", "Landroid/location/LocationManager;", "repository", "Lcom/ecostep/data/repository/EcoRepository;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "segmentStartTime", "sensorManager", "Landroid/hardware/SensorManager;", "buildNotif", "Landroid/app/Notification;", "createChannel", "", "onAccuracyChanged", "sensor", "accuracy", "", "onBind", "", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onLocationChanged", "location", "Landroid/location/Location;", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "onStartCommand", "flags", "startId", "saveCurrentSegment", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startTracking", "stopTracking", "todayDate", "", "kotlin.jvm.PlatformType", "updateNotif", "Companion", "app_debug"})
public final class TrackingService extends android.app.Service implements android.hardware.SensorEventListener, android.location.LocationListener {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "ecostep_channel";
    public static final int NOTIF_ID = 1;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.ecostep.STOP";
    private static final long SEGMENT_SAVE_INTERVAL_MS = 30000L;
    private android.hardware.SensorManager sensorManager;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor accelerometer;
    private android.location.LocationManager locationManager;
    @org.jetbrains.annotations.NotNull()
    private final com.ecostep.util.MovementDetector detector = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private com.ecostep.data.repository.EcoRepository repository;
    private double lastLat = 0.0;
    private double lastLng = 0.0;
    private long lastLocationTime = 0L;
    private float currentSpeedKmh = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private com.ecostep.data.model.MovementMode currentSegmentMode = com.ecostep.data.model.MovementMode.STATIONARY;
    private long segmentStartTime;
    @org.jetbrains.annotations.NotNull()
    public static final com.ecostep.service.TrackingService.Companion Companion = null;
    
    public TrackingService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Void onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void startTracking() {
    }
    
    private final void stopTracking() {
    }
    
    @java.lang.Override()
    public void onSensorChanged(@org.jetbrains.annotations.NotNull()
    android.hardware.SensorEvent event) {
    }
    
    @java.lang.Override()
    public void onAccuracyChanged(@org.jetbrains.annotations.Nullable()
    android.hardware.Sensor sensor, int accuracy) {
    }
    
    @java.lang.Override()
    public void onLocationChanged(@org.jetbrains.annotations.NotNull()
    android.location.Location location) {
    }
    
    private final java.lang.Object saveCurrentSegment(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void createChannel() {
    }
    
    private final android.app.Notification buildNotif() {
        return null;
    }
    
    private final void updateNotif() {
    }
    
    private final java.lang.String todayDate() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/ecostep/service/TrackingService$Companion;", "", "()V", "ACTION_STOP", "", "CHANNEL_ID", "NOTIF_ID", "", "SEGMENT_SAVE_INTERVAL_MS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}