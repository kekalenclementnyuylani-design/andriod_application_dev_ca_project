import 'dart:async';
import 'dart:math';
import 'package:sensors_plus/sensors_plus.dart';
import '../utils/constants.dart';

class SensorService {
  StreamSubscription? _accelerometerSubscription;
  double _baseX = 0, _baseY = 0, _baseZ = 0;
  bool _baselineCaptured = false;
  Function? onMotionDetected;

  void startMonitoring() {
    Future.delayed(const Duration(seconds: 1), () {
      _accelerometerSubscription =
          accelerometerEventStream().listen((AccelerometerEvent event) {
        if (!_baselineCaptured) {
          _baseX = event.x;
          _baseY = event.y;
          _baseZ = event.z;
          _baselineCaptured = true;
        } else {
          double delta = sqrt(
            pow(event.x - _baseX, 2) +
            pow(event.y - _baseY, 2) +
            pow(event.z - _baseZ, 2),
          );
          if (delta > AppConstants.movementThreshold) {
            onMotionDetected?.call();
          }
        }
      });
    });
  }

  void stopMonitoring() {
    _accelerometerSubscription?.cancel();
    _accelerometerSubscription = null;
    _baselineCaptured = false;
  }
}
