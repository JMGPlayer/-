package com.example

import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files

class DrivingEventAnalyzerTest {
    @Test fun sustainedEventsCountOnceAndSeparateAfterCruising() {
        val a = DrivingEventAnalyzer()
        a.sample(1_000_000_000, 0.0)
        a.sample(1_500_000_000, 2.0)
        assertEquals(0, a.accelerationEvents)
        a.sample(2_000_000_000, 4.0)
        a.sample(2_500_000_000, 6.0)
        assertEquals(1, a.accelerationEvents)
        a.sample(3_000_000_000, 6.0)
        a.sample(4_000_000_000, 10.0)
        assertEquals(2, a.accelerationEvents)
        a.sample(5_000_000_000, 6.0)
        a.sample(6_000_000_000, 2.0)
        assertEquals(1, a.brakingEvents)
        assertEquals(5.0, a.validSeconds, 0.001)
    }

    @Test fun gapsStaleSignalsReverseAndShortSpikesDoNotBecomeEvents() {
        val a = DrivingEventAnalyzer()
        a.sample(1_000_000_000, 0.0)
        a.sample(1_500_000_000, 2.0)
        a.gap()
        a.sample(2_000_000_000, 4.0)
        a.sample(2_500_000_000, 6.0)
        a.sample(2_500_000_000, 100.0)
        a.sample(3_000_000_000, -20.0)
        a.sample(4_000_000_000, 0.0)
        a.sample(8_000_000_000, 100.0)
        a.sample(8_010_000_000, 200.0)
        a.sample(9_000_000_000, Double.NaN)
        assertEquals(0, a.accelerationEvents)
        assertEquals(0, a.brakingEvents)
    }

    @Test fun journalSummaryAnalyzesOldFormatAndRespectsGapRows() {
        val dir = Files.createTempDirectory("events-test").toFile()
        try {
            val j = DriveJournal(dir, 1000, 0)
            j.sample(1100, 100, 1_000_000_000, 0f)
            j.sample(2100, 1100, 2_000_000_000, 4f)
            j.gap(2200, 1200, "unavailable")
            j.sample(3100, 2100, 3_000_000_000, 0f)
            j.finish(3200, 2200, "user_stop")
            val r = DriveJournal.read(j.file)
            assertEquals(1, r.accelerationEvents)
            assertEquals(0, r.brakingEvents)
            assertEquals(1.0, r.analyzedSeconds, 0.001)
        } finally { dir.deleteRecursively() }
    }
}
