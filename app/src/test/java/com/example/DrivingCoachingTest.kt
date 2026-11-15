package com.example

import org.junit.Assert.*
import org.junit.Test

class DrivingCoachingTest {
    private fun record() = DriveJournal.Companion.Summary("test", 1, 10000, 20, 60.0, 0, "저장 완료", 0, 0, 9.0)

    @Test fun noDataStationaryAndNoCandidatesAreDistinct() {
        assertEquals("분석 데이터 부족", DrivingCoaching.from(record().copy(analyzedSeconds = 0.0)).title)
        assertEquals("정지 상태 기록", DrivingCoaching.from(record().copy(maxKmh = 0.0)).title)
        val feedback = DrivingCoaching.from(record())
        assertEquals("기준 충족 후보 없음", feedback.title)
        assertTrue(feedback.message.contains("안전 운전 판정은 아닙니다"))
    }
    @Test fun eventTypesProduceSpecificCoaching() {
        assertEquals("가속 변화 확인", DrivingCoaching.from(record().copy(accelerationEvents = 2)).title)
        assertEquals("감속 변화 확인", DrivingCoaching.from(record().copy(brakingEvents = 1)).title)
        val both = DrivingCoaching.from(record().copy(accelerationEvents = 2, brakingEvents = 1))
        assertTrue(both.message.contains("급가속 후보 2회"))
        assertTrue(both.message.contains("급감속 후보 1회"))
    }
    @Test fun partialRecordsKeepLimitations() {
        assertEquals(2, DrivingCoaching.from(record().copy(state = "중단됨 · 종료 기록 없음", gaps = 2)).limitations.size)
        assertTrue(DrivingCoaching.from(record().copy(state = "기록 중")).limitations.single().contains("임시"))
        assertTrue(DrivingCoaching.from(record()).limitations.isEmpty())
    }
}
