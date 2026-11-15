package com.example

import java.io.File
import java.util.UUID
import kotlin.math.abs

/** Append-only journal. All calls are made on the service's single IO worker. */
class DriveJournal(directory: File, wallMs: Long, elapsedMs: Long) {
    val file: File
    private val writer: java.io.BufferedWriter
    private var closed = false
    private var lastSignalNs = -1L
    var samples = 0
        private set

    init {
        check(directory.exists() || directory.mkdirs()) { "기록 폴더 생성 실패" }
        check(directory.usableSpace > 1024 * 1024) { "저장 공간 부족" }
        file = File(directory, "trip_${wallMs}_${UUID.randomUUID()}.csv")
        writer = file.bufferedWriter()
        try {
            writer.write("type,wall_ms,elapsed_ms,signal_ns,speed_mps,detail\n")
            row("start,$wallMs,$elapsedMs,,,veh_sonata")
        } catch (e: Exception) {
            writer.close()
            throw e
        }
    }

    private fun row(value: String) {
        check(!closed)
        writer.write(value)
        writer.newLine()
        writer.flush()
    }

    fun sample(wallMs: Long, elapsedMs: Long, signalNs: Long, speedMps: Float) {
        if (!speedMps.isFinite() || signalNs <= 0 || signalNs <= lastSignalNs) {
            gap(wallMs, elapsedMs, "invalid_or_stale")
            return
        }
        if (lastSignalNs > 0 && signalNs - lastSignalNs > 3_000_000_000L) {
            gap(wallMs, elapsedMs, "signal_gap")
        }
        row("sample,$wallMs,$elapsedMs,$signalNs,$speedMps,")
        lastSignalNs = signalNs
        samples++
    }

    fun gap(wallMs: Long, elapsedMs: Long, reason: String) {
        row("gap,$wallMs,$elapsedMs,,,${reason.replace(Regex("[^a-zA-Z0-9_]"), "_")}")
    }

    fun finish(wallMs: Long, elapsedMs: Long, reason: String) {
        if (closed) return
        try { row("end,$wallMs,$elapsedMs,,,$reason") }
        finally { closed = true; writer.close() }
    }

    fun abort() {
        if (!closed) { closed = true; writer.close() }
    }

    companion object {
        data class Summary(
            val name: String, val startedMs: Long, val durationMs: Long,
            val count: Int, val maxKmh: Double?, val gaps: Int, val state: String,
            val accelerationEvents: Int, val brakingEvents: Int, val analyzedSeconds: Double
        )

        fun read(file: File, activeName: String? = null): Summary {
            val analyzer = DrivingEventAnalyzer()
            var started = 0L
            var firstElapsed = 0L
            var lastElapsed = 0L
            var count = 0
            var gaps = 0
            var maximum: Double? = null
            var end: String? = null
            file.useLines { lines -> lines.drop(1).forEach { line ->
                val p = line.split(',')
                if (p.size != 6) { analyzer.gap(); return@forEach } // A truncated final row after process death.
                val wall = p[1].toLongOrNull() ?: return@forEach
                val elapsed = p[2].toLongOrNull() ?: return@forEach
                when (p[0]) {
                    "start" -> { started = wall; firstElapsed = elapsed; lastElapsed = elapsed }
                    "sample" -> {
                        val speed = p[4].toDoubleOrNull()?.takeIf { it.isFinite() }
                        if (speed == null) { analyzer.gap(); return@forEach }
                        analyzer.sample(p[3].toLongOrNull() ?: 0L, speed)
                        count++; maximum = maxOf(maximum ?: 0.0, abs(speed) * 3.6)
                        lastElapsed = elapsed
                    }
                    "gap" -> { analyzer.gap(); gaps++; lastElapsed = elapsed }
                    "end" -> { end = p[5]; lastElapsed = elapsed }
                }
            } }
            val state = when {
                end == "user_stop" -> "저장 완료"
                end != null -> "중단됨 ($end)"
                file.name == activeName -> "기록 중"
                else -> "중단됨 · 종료 기록 없음"
            }
            return Summary(file.name, started, (lastElapsed-firstElapsed).coerceAtLeast(0), count, maximum, gaps, state,
                analyzer.accelerationEvents, analyzer.brakingEvents, analyzer.validSeconds)
        }
    }
}
