# EcoStep 🌍
### Personal Carbon Footprint Tracker — Built with Kotlin

---

## What it does
EcoStep silently tracks how you move throughout the day using your phone's sensors — no manual input needed. It detects whether you are walking, running, or in a vehicle, calculates the CO₂ cost of every trip, and builds your personal environmental impact report over time.

## Sensors used
- **Accelerometer** — detects movement patterns (walking rhythm vs vehicle vibration)
- **GPS** — measures distance traveled and confirms speed for accurate mode detection

## How detection works
Each movement mode has a unique accelerometer signature:
- Walking = rhythmic peaks at 1.5-2 steps/second, speed under 9 km/h
- Running = stronger faster peaks at 2.5-3 steps/second
- Vehicle = smooth continuous vibration + GPS speed above 12 km/h
- Stationary = near-zero movement, speed under 2 km/h

## Carbon calculation
- Walking / Running = 0g CO₂ per km
- Vehicle = ~120g CO₂ per km (average petrol car)

---

## Setup in Android Studio
1. Open Android Studio → File > Open → select the EcoStep folder
2. Wait for Gradle sync to complete
3. Connect your Android device (API 26+) with USB debugging enabled
4. Click Run

## Required permissions (granted at runtime on first launch)
- Location (Fine + Coarse) — for GPS distance tracking
- Activity Recognition — for movement detection
- Notifications — for foreground service notification

## Demo script for class presentation
1. Open app — show the home dashboard (0.00 kg CO₂, Stationary)
2. Tap Start Tracking — Live screen appears
3. Walk across the room — mode switches to Walking, CO₂ stays 0g
4. Shake the phone fast in a vehicle-like pattern — mode switches to Vehicle, CO₂ counter starts ticking up
5. Go back to walking — mode switches back, CO₂ stops increasing
6. Tap Weekly History — show the day-by-day carbon bar chart
7. Point out: zero manual input, everything automatic from sensors

---

## Project structure
```
app/src/main/java/com/ecostep/
├── data/
│   ├── model/Models.kt              — MovementMode, TripSegment, DailySummary, LiveState
│   └── repository/EcoRepository.kt — Room DB + DAOs
├── service/
│   └── TrackingService.kt           — background engine: accelerometer + GPS + CO2 calc
├── ui/
│   ├── home/MainActivity.kt         — home dashboard with today's CO2 summary
│   ├── session/LiveActivity.kt      — real-time mode + speed + CO2 screen
│   ├── history/HistoryActivity.kt   — weekly breakdown by day
│   └── report/DailyReportActivity.kt — detailed daily report
├── util/
│   ├── MovementDetector.kt          — classifies mode from raw accelerometer + GPS data
│   └── TrackingManager.kt           — singleton holding all live tracking state
└── receiver/
    └── BootReceiver.kt              — clears tracking state on device reboot
```
