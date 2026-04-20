import 'dart:async';
import 'package:audioplayers/audioplayers.dart';
import 'package:vibration/vibration.dart';
import 'package:torch_light/torch_light.dart';

class AlarmService {
  final AudioPlayer _audioPlayer = AudioPlayer();
  Timer? _flashTimer;
  bool _isFlashOn = false;
  bool _isAlarming = false;

  bool get isAlarming => _isAlarming;

  Future<void> startAlarm() async {
    if (_isAlarming) return;
    _isAlarming = true;
    await _audioPlayer.setVolume(1.0);
    await _audioPlayer.setReleaseMode(ReleaseMode.loop);
    await _audioPlayer.play(AssetSource('sounds/alarm.mp3'));
    if (await Vibration.hasVibrator() ?? false) {
      Vibration.vibrate(pattern: [500, 500], repeat: 0);
    }
    _startFlashing();
  }

  void _startFlashing() {
    _flashTimer = Timer.periodic(const Duration(milliseconds: 300), (_) async {
      try {
        if (_isFlashOn) {
          await TorchLight.disableTorch();
        } else {
          await TorchLight.enableTorch();
        }
        _isFlashOn = !_isFlashOn;
      } catch (_) {}
    });
  }

  Future<void> stopAlarm() async {
    if (!_isAlarming) return;
    _isAlarming = false;
    await _audioPlayer.stop();
    Vibration.cancel();
    _flashTimer?.cancel();
    _flashTimer = null;
    try {
      await TorchLight.disableTorch();
    } catch (_) {}
    _isFlashOn = false;
  }

  void dispose() {
    stopAlarm();
    _audioPlayer.dispose();
  }
}
