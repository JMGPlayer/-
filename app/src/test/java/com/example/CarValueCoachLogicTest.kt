package com.example

import com.example.data.model.DataSourceType
import com.example.data.model.RouteCategory
import com.example.data.repository.CarValueRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarValueCoachLogicTest {

    private lateinit var repository: CarValueRepository

    @Before
    fun setUp() {
        repository = CarValueRepository()
    }

    @Test
    fun testInitialVehicleDataAndOdometer() {
        val profile = repository.vehicleProfile.value
        assertEquals("쏘나타 디 엣지 하이브리드", profile.modelName)
        assertEquals(28450, profile.currentMileageKm)
    }

    @Test
    fun testSelectCoachingGoalFocusesSingleTask() {
        repository.selectCoachingGoal("goal_idle_stop")
        val goals = repository.coachingGoals.value
        val focusedGoals = goals.filter { it.isCurrentFocus }
        assertEquals(1, focusedGoals.size)
        assertEquals("goal_idle_stop", focusedGoals.first().id)
    }

    @Test
    fun testAddDrivingSessionUpdatesMileageAndGeneratesReport() {
        val initialMileage = repository.vehicleProfile.value.currentMileageKm
        repository.addDrivingSession(
            title = "퇴근길 테스트 주행",
            distanceKm = 15.0,
            durationMinutes = 30,
            averageSpeedKmh = 30,
            topSpeedKmh = 65,
            efficiency = 20.1,
            routeCategory = RouteCategory.COMMUTE,
            isComparable = true
        )

        val updatedProfile = repository.vehicleProfile.value
        assertEquals(initialMileage + 15, updatedProfile.currentMileageKm)

        val report = repository.getTransparencyReport()
        assertTrue(report.comparableTripsCount >= 3)
        assertTrue(report.sensorCollectionRatio >= 80f)
        assertTrue(report.missingOrGapRatio > 0f)
        assertTrue(report.documentVerifiedMaintenanceRatio >= 90f)
    }

    @Test
    fun testNewCarEmptyScenarioState() {
        repository.applyDemoScenario(com.example.data.repository.DemoScenarioType.NEW_CAR_EMPTY)
        val profile = repository.vehicleProfile.value
        assertEquals(45, profile.currentMileageKm)
        assertTrue(repository.drivingSessions.value.isEmpty())
        assertTrue(repository.maintenanceRecords.value.isEmpty())

        val report = repository.getTransparencyReport()
        assertEquals(0, report.totalTripsCount)
        assertEquals(0, report.comparableTripsCount)
        assertEquals("신차 준비 중", report.transparencyGrade)
    }

    @Test
    fun testSevereConditionAlertScenarioState() {
        repository.applyDemoScenario(com.example.data.repository.DemoScenarioType.SEVERE_CONDITION_ALERT)
        val profile = repository.vehicleProfile.value
        assertEquals(33800, profile.currentMileageKm)
        assertEquals(com.example.data.model.UsageCondition.SEVERE, profile.usageCondition)

        val schedules = repository.maintenanceSchedules.value
        val oilSchedule = schedules.first { it.id == "sched_engine_oil" }
        assertEquals(com.example.data.model.ScheduleStatus.OVERDUE, oilSchedule.status)

        val trips = repository.drivingSessions.value
        val pendingTrips = trips.filter { it.isPendingJudgment }
        assertTrue(pendingTrips.isNotEmpty())
    }

    @Test
    fun testIoniq5EvScenario() {
        repository.applyDemoScenario(com.example.data.repository.DemoScenarioType.IONIQ5_EV)
        val profile = repository.vehicleProfile.value
        assertEquals(com.example.data.model.PowerTrainType.EV, profile.powerTrainType)
        assertEquals(74, profile.batterySocPercent)
        assertEquals("km/kWh", profile.powerTrainType.efficiencyUnit)

        val goals = repository.coachingGoals.value
        assertTrue(goals.any { it.title.contains("회생제동") })

        val report = repository.getTransparencyReport()
        assertEquals(com.example.data.model.PowerTrainType.EV, report.powerTrainType)
    }

    @Test
    fun testDynamicUsageConditionToggle() {
        repository.updateUsageCondition(com.example.data.model.UsageCondition.SEVERE)
        assertEquals(com.example.data.model.UsageCondition.SEVERE, repository.vehicleProfile.value.usageCondition)

        val schedules = repository.maintenanceSchedules.value
        val oil = schedules.first { it.id == "sched_engine_oil" }
        // In severe condition, nextDueKm is lastServicedKm + severeIntervalKm (5,000)
        assertEquals(oil.lastServicedKm + oil.severeIntervalKm, oil.nextDueKm)
    }

    @Test
    fun testSaveTripSituationMemo() {
        val firstTrip = repository.drivingSessions.value.first()
        repository.saveTripSituationMemo(firstTrip.id, "강풍 및 급격한 우회 도로 정체로 연비 저하")

        val updatedTrip = repository.drivingSessions.value.first { it.id == firstTrip.id }
        assertEquals("강풍 및 급격한 우회 도로 정체로 연비 저하", updatedTrip.userSituationMemo)
    }

    @Test
    fun testTransparencyReportPrivacyMasking() {
        val unmaskedReport = repository.getTransparencyReport(isMaskPlate = false, isMaskRoute = false)
        assertFalse(unmaskedReport.plateNumber.contains("****"))

        val maskedReport = repository.getTransparencyReport(isMaskPlate = true, isMaskRoute = true)
        assertTrue(maskedReport.isMaskPlate)
        assertTrue(maskedReport.plateNumber.contains("****"))
        assertTrue(maskedReport.isMaskRouteLocation)
    }

    @Test
    fun testCustomDriverRegistrationAndRemoval() {
        val initialDrivers = repository.registeredDrivers.value
        assertTrue(initialDrivers.contains("김민준 (주 운전자 · 본인)"))
        assertFalse(initialDrivers.any { it.contains("미확인 게스트") })

        repository.addCustomDriver("박서준 (가족 · 주말 이용)")
        val updatedDrivers = repository.registeredDrivers.value
        assertTrue(updatedDrivers.contains("박서준 (가족 · 주말 이용)"))
        assertEquals("박서준 (가족 · 주말 이용)", repository.vehicleProfile.value.currentDriver)

        repository.removeCustomDriver("박서준 (가족 · 주말 이용)")
        assertFalse(repository.registeredDrivers.value.contains("박서준 (가족 · 주말 이용)"))
    }

    @Test
    fun testAppSettingsDefaults() {
        val defaults = com.example.data.model.AppSettings()
        assertFalse(defaults.isDarkMode)
        assertTrue(defaults.autoDetectDrive)
        assertTrue(defaults.coachingAlertsEnabled)
        assertEquals(2000, defaults.maintenanceAlertThresholdKm)
    }
}
