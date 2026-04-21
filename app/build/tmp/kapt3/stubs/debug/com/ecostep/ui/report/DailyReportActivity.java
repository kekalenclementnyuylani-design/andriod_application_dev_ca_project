package com.ecostep.ui.report;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J0\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nH\u0002J\b\u0010\u000f\u001a\u00020\bH\u0002J\u0010\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0012\u0010\u0013\u001a\u00020\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\u0010\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/ecostep/ui/report/DailyReportActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "b", "Lcom/ecostep/databinding/ActivityDailyReportBinding;", "repo", "Lcom/ecostep/data/repository/EcoRepository;", "displayData", "", "co2Grams", "", "savedGrams", "walk", "run", "veh", "loadCurrentData", "loadHistoricalData", "date", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "provideFeedback", "currentCo2", "app_debug"})
public final class DailyReportActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.ecostep.databinding.ActivityDailyReportBinding b;
    private com.ecostep.data.repository.EcoRepository repo;
    
    public DailyReportActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadCurrentData() {
    }
    
    private final void loadHistoricalData(java.lang.String date) {
    }
    
    private final void displayData(double co2Grams, double savedGrams, double walk, double run, double veh) {
    }
    
    private final void provideFeedback(double currentCo2) {
    }
}