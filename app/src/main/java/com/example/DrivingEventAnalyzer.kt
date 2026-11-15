package com.example

/** Demo-only forward-speed event detector; not a vehicle-health assessment. */
class DrivingEventAnalyzer {
    var accelerationEvents = 0
        private set
    var brakingEvents = 0
        private set
    var validSeconds = 0.0
        private set
    private var previousNs: Long? = null
    private var previousSpeed = 0.0
    private var direction = 0
    private var eventSeconds = 0.0
    private var counted = false

    fun gap() {
        previousNs = null
        resetEvent()
    }

    private fun resetEvent() {
        direction = 0
        eventSeconds = 0.0
        counted = false
    }

    fun sample(signalNs: Long, speedMps: Double) {
        if (signalNs <= 0 || !speedMps.isFinite() || speedMps < 0) {
            gap()
            return
        }
        val previous = previousNs
        if (previous != null && signalNs <= previous) {
            gap()
            return
        }
        previousNs = signalNs
        val oldSpeed = previousSpeed
        previousSpeed = speedMps
        if (previous == null) return
        val seconds = (signalNs - previous) / 1_000_000_000.0
        if (seconds < 0.1 || seconds > 3.0) {
            resetEvent()
            return
        }
        validSeconds += seconds
        val acceleration = (speedMps - oldSpeed) / seconds
        val nextDirection = when {
            acceleration >= 3.0 -> 1
            acceleration <= -3.0 -> -1
            else -> 0
        }
        if (nextDirection == 0) {
            resetEvent()
            return
        }
        if (direction != nextDirection) resetEvent()
        direction = nextDirection
        eventSeconds += seconds
        if (!counted && eventSeconds >= 0.8) {
            if (direction == 1) accelerationEvents++ else brakingEvents++
            counted = true
        }
    }
}
