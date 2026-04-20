import 'package:flutter/material.dart';
import '../services/sensor_service.dart';
import '../services/alarm_service.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final SensorService _sensorService = SensorService();
  final AlarmService _alarmService = AlarmService();
  bool _isProtectionActive = false;
  bool _isAlarming = false;

  @override
  void initState() {
    super.initState();
    _sensorService.onMotionDetected = _onMotionDetected;
  }

  void _onMotionDetected() {
    if (!_isAlarming) {
      setState(() => _isAlarming = true);
      _alarmService.startAlarm();
    }
  }

  void _toggleProtection() {
    setState(() {
      _isProtectionActive = !_isProtectionActive;
      _isAlarming = false;
    });
    if (_isProtectionActive) {
      _sensorService.startMonitoring();
    } else {
      _sensorService.stopMonitoring();
      _alarmService.stopAlarm();
    }
  }

  void _stopAlarm() {
    setState(() => _isAlarming = false);
    _alarmService.stopAlarm();
    _sensorService.stopMonitoring();
    Future.delayed(const Duration(milliseconds: 500), () {
      if (_isProtectionActive) _sensorService.startMonitoring();
    });
  }

  @override
  void dispose() {
    _sensorService.stopMonitoring();
    _alarmService.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: _isAlarming
          ? Colors.red.shade900
          : (_isProtectionActive ? Colors.green.shade900 : Colors.grey.shade900),
      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                '🔐 Anti-Theft Alarm',
                style: TextStyle(color: Colors.white, fontSize: 28, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              Text(
                _isAlarming ? '🚨 MOTION DETECTED!' : (_isProtectionActive ? '✅ Protection Active' : '🔓 Protection Off'),
                style: TextStyle(
                  color: _isAlarming ? Colors.yellow : Colors.white70,
                  fontSize: 18,
                  fontWeight: _isAlarming ? FontWeight.bold : FontWeight.normal,
                ),
              ),
              const SizedBox(height: 60),
              Icon(
                _isAlarming ? Icons.warning_rounded : (_isProtectionActive ? Icons.shield : Icons.shield_outlined),
                size: 140,
                color: _isAlarming ? Colors.yellow : (_isProtectionActive ? Colors.greenAccent : Colors.white38),
              ),
              const SizedBox(height: 60),
              if (_isAlarming)
                ElevatedButton.icon(
                  onPressed: _stopAlarm,
                  icon: const Icon(Icons.volume_off),
                  label: const Text('STOP ALARM'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.yellow,
                    foregroundColor: Colors.black,
                    padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 18),
                    textStyle: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
                  ),
                ),
              if (!_isAlarming)
                ElevatedButton.icon(
                  onPressed: _toggleProtection,
                  icon: Icon(_isProtectionActive ? Icons.lock_open : Icons.lock),
                  label: Text(_isProtectionActive ? 'Stop Protection' : 'Start Protection'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: _isProtectionActive ? Colors.redAccent : Colors.greenAccent,
                    foregroundColor: Colors.black,
                    padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 18),
                    textStyle: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
                  ),
                ),
              const SizedBox(height: 30),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 40),
                child: Text(
                  _isProtectionActive
                      ? 'Place your phone down and walk away. Alarm triggers on movement.'
                      : 'Tap Start Protection to activate the alarm.',
                  textAlign: TextAlign.center,
                  style: const TextStyle(color: Colors.white54, fontSize: 14),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
