package com.example

import org.junit.Test
import org.junit.Assert.*
import java.nio.file.Files

class DriveJournalTest {
    @Test fun savesSignedSpeedAndSeparatesMissingDataFromZero() {
        val dir = Files.createTempDirectory("drive-test").toFile()
        try {
            val j = DriveJournal(dir, 1000, 500)
            j.sample(1100, 600, 1_000_000_000, 0f)
            j.gap(1200, 700, "unavailable")
            j.sample(1300, 800, 2_000_000_000, -10f)
            j.sample(1400, 900, 2_000_000_000, 100f) // stale: excluded
            j.finish(1500, 1000, "user_stop")
            val r = DriveJournal.read(j.file)
            assertEquals(2, r.count)
            assertEquals(36.0, r.maxKmh!!, 0.0001)
            assertEquals(2, r.gaps)
            assertEquals(500L, r.durationMs)
            assertEquals("저장 완료", r.state)
            assertTrue(j.file.readText().contains(",-10.0,"))
        } finally { dir.deleteRecursively() }
    }

    @Test fun interruptedFileSurvivesAndNewSessionDoesNotOverwrite() {
        val dir = Files.createTempDirectory("drive-test").toFile()
        try {
            val first = DriveJournal(dir, 1000, 0)
            first.sample(1100, 100, 1_000_000_000, 5f)
            first.abort()
            first.file.appendText("sample,broken")
            val second = DriveJournal(dir, 1000, 200)
            second.finish(1400, 400, "user_stop")
            val r = DriveJournal.read(first.file)
            assertEquals(1, r.count)
            assertTrue(r.state.startsWith("중단됨"))
            assertNotEquals(first.file.name, second.file.name)
            assertNull(DriveJournal.read(second.file).maxKmh)
        } finally { dir.deleteRecursively() }
    }
}
