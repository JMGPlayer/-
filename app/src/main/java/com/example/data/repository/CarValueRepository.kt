package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class DemoScenarioType(val title: String, val description: String) {
    STANDARD_VERIFIED(
        title = "쏘나타 하이브리드 (AAA 등급)",
        description = "14개월 28,450km 주행, 블루핸즈 정비 3건 전건 증빙, 반복 출퇴근 연비 코칭"
    ),
    IONIQ5_EV(
        title = "아이오닉 5 전기차 (EV 전비·배터리)",
        description = "전비 6.2 km/kWh, SOC 74%, 회생제동 탄력 주행 코칭, 감속기 오일 점검"
    ),
    NEW_CAR_EMPTY(
        title = "신차 출고 (초기 빈 상태)",
        description = "누적 45km, 주행 및 정비 기록 0건. 첫 사용자 온보딩 및 빈 화면 검증용"
    ),
    SEVERE_CONDITION_ALERT(
        title = "가혹 조건 운행 & 점검 임박",
        description = "상습 정체 가혹 주기(5,000km) 적용, 엔진오일 점검 권장, 악천후 판단보류"
    )
}

class CarValueRepository {

    private val _currentScenario = MutableStateFlow(DemoScenarioType.STANDARD_VERIFIED)
    val currentScenario: StateFlow<DemoScenarioType> = _currentScenario.asStateFlow()

    private val _registeredDrivers = MutableStateFlow(
        listOf(
            "김민준 (주 운전자 · 본인)",
            "이수진 (공동 운전자 · 배우자)",
            "김태호 (가족 · 주말 이용)"
        )
    )
    val registeredDrivers: StateFlow<List<String>> = _registeredDrivers.asStateFlow()

    private val _garageVehicles = MutableStateFlow(listOf(
        VehicleProfile(
            id = "veh_sonata",
            nickname = "1호차 (출퇴근 메인)",
            modelName = "쏘나타 디 엣지 하이브리드",
            plateNumber = "348두 7192",
            modelYear = 2024,
            currentMileageKm = 28450,
            powerTrainType = PowerTrainType.HYBRID,
            fuelType = "하이브리드 (가솔린+전기)",
            officialEfficiency = "19.4 km/L",
            sensorConnectionStatus = "차량 센서 정상 연동 중 (Vehicle SDK)",
            currentDriver = "김민준 (주 운전자 · 본인)",
            usageCondition = UsageCondition.NORMAL
        ),
        VehicleProfile(
            id = "veh_ioniq5",
            nickname = "2호차 (주말 패밀리 EV)",
            modelName = "아이오닉 5 롱레인지 2WD",
            plateNumber = "125하 4820",
            modelYear = 2024,
            currentMileageKm = 16820,
            powerTrainType = PowerTrainType.EV,
            fuelType = "전기차 (배터리 EV)",
            officialEfficiency = "5.2 km/kWh",
            sensorConnectionStatus = "차량 센서 정상 연동 중 (Vehicle SDK)",
            currentDriver = "이수진 (공동 운전자 · 배우자)",
            usageCondition = UsageCondition.NORMAL,
            batterySocPercent = 74,
            batterySohPercent = 99
        ),
        VehicleProfile(
            id = "veh_genesis",
            nickname = "3호차 (패밀리 세단)",
            modelName = "제네시스 G80 2.5T AWD",
            plateNumber = "58노 1039",
            modelYear = 2023,
            currentMileageKm = 42100,
            powerTrainType = PowerTrainType.GASOLINE,
            fuelType = "가솔린 내연기관",
            officialEfficiency = "10.8 km/L",
            sensorConnectionStatus = "차량 센서 정상 연동 중 (Vehicle SDK)",
            currentDriver = "김민준 (주 운전자 · 본인)",
            usageCondition = UsageCondition.NORMAL
        )
    ))
    val garageVehicles: StateFlow<List<VehicleProfile>> = _garageVehicles.asStateFlow()

    private val _vehicleProfile = MutableStateFlow(_garageVehicles.value.first())
    val vehicleProfile: StateFlow<VehicleProfile> = _vehicleProfile.asStateFlow()

    private val _drivingSessions = MutableStateFlow<List<DrivingSession>>(getStandardDrivingSessions())
    val drivingSessions: StateFlow<List<DrivingSession>> = _drivingSessions.asStateFlow()

    private val _coachingGoals = MutableStateFlow<List<CoachingGoal>>(getStandardCoachingGoals())
    val coachingGoals: StateFlow<List<CoachingGoal>> = _coachingGoals.asStateFlow()

    private val _maintenanceSchedules = MutableStateFlow<List<MaintenanceScheduleItem>>(getStandardMaintenanceSchedules(UsageCondition.NORMAL))
    val maintenanceSchedules: StateFlow<List<MaintenanceScheduleItem>> = _maintenanceSchedules.asStateFlow()

    private val _maintenanceRecords = MutableStateFlow<List<MaintenanceRecord>>(getStandardMaintenanceRecords())
    val maintenanceRecords: StateFlow<List<MaintenanceRecord>> = _maintenanceRecords.asStateFlow()

    fun selectGarageVehicle(vehicleId: String) {
        val target = _garageVehicles.value.firstOrNull { it.id == vehicleId } ?: return
        _vehicleProfile.value = target
        if (target.powerTrainType == PowerTrainType.EV) {
            _drivingSessions.value = getEvDrivingSessions()
            _coachingGoals.value = getEvCoachingGoals()
            _maintenanceSchedules.value = getEvMaintenanceSchedules()
            _maintenanceRecords.value = getEvMaintenanceRecords()
        } else {
            _drivingSessions.value = getStandardDrivingSessions()
            _coachingGoals.value = getStandardCoachingGoals()
            _maintenanceSchedules.value = getStandardMaintenanceSchedules(target.usageCondition)
            _maintenanceRecords.value = getStandardMaintenanceRecords()
        }
    }

    fun addGarageVehicle(nickname: String, modelName: String, plateNumber: String, powerTrain: PowerTrainType, driver: String) {
        val newVehicle = VehicleProfile(
            id = "veh_${System.currentTimeMillis()}",
            nickname = nickname,
            modelName = modelName,
            plateNumber = plateNumber,
            modelYear = 2024,
            currentMileageKm = 12000,
            powerTrainType = powerTrain,
            fuelType = powerTrain.label,
            officialEfficiency = if (powerTrain == PowerTrainType.EV) "5.5 km/kWh" else "15.0 km/L",
            currentDriver = driver,
            usageCondition = UsageCondition.NORMAL
        )
        _garageVehicles.update { it + newVehicle }
        selectGarageVehicle(newVehicle.id)
    }

    fun updateUsageCondition(condition: UsageCondition) {
        _vehicleProfile.update { it.copy(usageCondition = condition) }
        _garageVehicles.update { list ->
            list.map { if (it.id == _vehicleProfile.value.id) it.copy(usageCondition = condition) else it }
        }
        recalculateSchedulesForCondition(condition)
    }

    fun updateCurrentDriver(driverName: String) {
        val trimmed = driverName.trim()
        if (trimmed.isEmpty()) return
        _registeredDrivers.update { list ->
            if (list.contains(trimmed)) list else list + trimmed
        }
        _vehicleProfile.update { it.copy(currentDriver = trimmed) }
        _garageVehicles.update { list ->
            list.map { if (it.id == _vehicleProfile.value.id) it.copy(currentDriver = trimmed) else it }
        }
    }

    fun addCustomDriver(driverName: String) {
        updateCurrentDriver(driverName)
    }

    fun removeCustomDriver(driverName: String) {
        val currentList = _registeredDrivers.value
        if (currentList.size <= 1) return
        val updated = currentList.filterNot { it == driverName }
        _registeredDrivers.value = updated
        if (_vehicleProfile.value.currentDriver == driverName) {
            val fallback = updated.first()
            _vehicleProfile.update { it.copy(currentDriver = fallback) }
            _garageVehicles.update { list ->
                list.map { if (it.id == _vehicleProfile.value.id) it.copy(currentDriver = fallback) else it }
            }
        }
    }

    fun saveTripSituationMemo(tripId: Long, memo: String) {
        _drivingSessions.update { list ->
            list.map { trip ->
                if (trip.id == tripId) trip.copy(userSituationMemo = memo) else trip
            }
        }
    }

    private fun recalculateSchedulesForCondition(condition: UsageCondition) {
        val currentKm = _vehicleProfile.value.currentMileageKm
        _maintenanceSchedules.update { schedules ->
            schedules.map { s ->
                val interval = if (condition == UsageCondition.SEVERE) s.severeIntervalKm else s.normalIntervalKm
                val nextDue = s.lastServicedKm + interval
                val remainingKm = nextDue - currentKm
                val status = when {
                    remainingKm <= 0 -> ScheduleStatus.OVERDUE
                    remainingKm <= 2000 -> ScheduleStatus.APPROACHING
                    else -> ScheduleStatus.NORMAL
                }
                s.copy(
                    nextDueKm = nextDue,
                    status = status,
                    description = if (condition == UsageCondition.SEVERE) {
                        "가혹 조건 적용: 주기 ${String.format("%,d", interval)} km (잔여 ${String.format("%,d", remainingKm.coerceAtLeast(0))} km)"
                    } else {
                        "통상 조건 적용: 권장 ${String.format("%,d", interval)} km (잔여 ${String.format("%,d", remainingKm.coerceAtLeast(0))} km)"
                    }
                )
            }
        }
    }

    fun selectCoachingGoal(goalId: String) {
        _coachingGoals.update { list ->
            list.map { goal ->
                goal.copy(isCurrentFocus = (goal.id == goalId))
            }
        }
    }

    fun addDrivingSession(
        title: String,
        distanceKm: Double,
        durationMinutes: Int,
        averageSpeedKmh: Int,
        topSpeedKmh: Int,
        efficiency: Double,
        routeCategory: RouteCategory,
        isComparable: Boolean,
        isPending: Boolean = false,
        pendingReason: String? = null,
        userMemo: String? = null
    ) {
        val currentProfile = _vehicleProfile.value
        val newMileage = currentProfile.currentMileageKm + distanceKm.toInt()
        _vehicleProfile.update { it.copy(currentMileageKm = newMileage) }

        val activeGoal = _coachingGoals.value.firstOrNull { it.isCurrentFocus }
        val targetEff = if (currentProfile.powerTrainType == PowerTrainType.EV) 6.0 else 19.0
        val isAchieved = if (activeGoal != null && isComparable && !isPending) {
            efficiency >= targetEff
        } else null

        val effUnit = currentProfile.powerTrainType.efficiencyUnit

        val newSession = DrivingSession(
            id = System.currentTimeMillis(),
            title = title,
            dateFormatted = "2026.11.09 18:20",
            distanceKm = distanceKm,
            durationMinutes = durationMinutes,
            averageSpeedKmh = averageSpeedKmh,
            topSpeedKmh = topSpeedKmh,
            efficiency = efficiency,
            efficiencyUnit = effUnit,
            routeCategory = routeCategory,
            isComparable = isComparable,
            isPendingJudgment = isPending,
            pendingReason = pendingReason,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 1,
            decelEvaluationNote = "주행 데이터 정상 검증 및 안전 분석 적용 (감속 관측·원인 미확인)",
            coachingGoalMatched = activeGoal?.id,
            isCoachingGoalAchieved = isAchieved,
            speedSamples = listOf(0, 20, 35, averageSpeedKmh, topSpeedKmh, (averageSpeedKmh * 0.9).toInt(), 0),
            efficiencySamples = listOf(efficiency * 0.8, efficiency * 0.95, efficiency * 1.05, efficiency, 0.0),
            driverName = currentProfile.currentDriver,
            userSituationMemo = userMemo
        )

        _drivingSessions.update { listOf(newSession) + it }

        if (activeGoal != null && isAchieved == true) {
            _coachingGoals.update { list ->
                list.map { g ->
                    if (g.id == activeGoal.id) {
                        val newProgress = (g.progressCount + 1).coerceAtMost(g.totalTargetTrips)
                        g.copy(
                            progressCount = newProgress,
                            afterEfficiency = efficiency,
                            improvementSummary = "목표 달성 주행 누적! 최근 효율 $efficiency $effUnit"
                        )
                    } else g
                }
            }
        }
    }

    fun addMaintenanceRecord(
        workshopName: String,
        mileageKm: Int,
        items: List<String>,
        actionType: MaintenanceActionType = MaintenanceActionType.REPLACEMENT,
        totalCostWon: Int,
        isDocumentVerified: Boolean,
        verificationLevel: VerificationLevel = VerificationLevel.DATA_MATCHED,
        documentTitle: String,
        receiptMemo: String
    ) {
        val newRecord = MaintenanceRecord(
            id = System.currentTimeMillis(),
            dateFormatted = "2026.11.09",
            mileageKm = mileageKm,
            workshopName = workshopName,
            items = items,
            actionType = actionType,
            totalCostWon = totalCostWon,
            isDocumentVerified = isDocumentVerified,
            verificationLevel = verificationLevel,
            documentTitle = documentTitle,
            receiptMemo = receiptMemo
        )

        _maintenanceRecords.update { listOf(newRecord) + it }

        // Update schedule if matching items and action is replacement
        if (actionType == MaintenanceActionType.REPLACEMENT) {
            val condition = _vehicleProfile.value.usageCondition
            _maintenanceSchedules.update { schedules ->
                schedules.map { s ->
                    val matching = items.any { it.contains(s.name.take(4)) }
                    if (matching) {
                        val interval = if (condition == UsageCondition.SEVERE) s.severeIntervalKm else s.normalIntervalKm
                        s.copy(
                            lastServicedKm = mileageKm,
                            lastServicedDate = "2026.11.09",
                            nextDueKm = mileageKm + interval,
                            status = ScheduleStatus.NORMAL
                        )
                    } else s
                }
            }
        }
    }

    fun getTransparencyReport(isMaskPlate: Boolean = false, isMaskRoute: Boolean = false): TransparencyReport {
        val profile = _vehicleProfile.value
        val sessions = _drivingSessions.value
        val records = _maintenanceRecords.value
        val activeGoal = _coachingGoals.value.firstOrNull { it.isCurrentFocus }

        val totalTrackedKm = sessions.sumOf { it.distanceKm }.toInt() + (if (sessions.isEmpty()) 0 else 1850)
        val verifiedSensorCount = sessions.count { it.dataSource == DataSourceType.VEHICLE_SDK && it.dataQuality == DataQualityType.VERIFIED_COMPLETE }
        val sensorRatio = if (sessions.isNotEmpty()) (verifiedSensorCount.toFloat() / sessions.size) * 100f else 96.8f

        val verifiedRecordCount = records.count { it.isDocumentVerified }
        val docRatio = if (records.isNotEmpty()) (verifiedRecordCount.toFloat() / records.size) * 100f else 100f

        val comparableCount = sessions.count { it.isComparable && !it.isPendingJudgment }
        val pendingCount = sessions.count { it.isPendingJudgment }

        val certNote = if (sessions.isEmpty()) {
            "신차 상태로 아직 누적 주행 데이터가 수집되지 않았습니다. 첫 주행 후 리포트가 생성됩니다."
        } else {
            "본 리포트는 차량 센서 직접 수집 데이터와 정비 명세서 원본 증빙으로 교차 검증된 투명 관리 이력입니다."
        }

        val earnedIntegrity = if (sessions.isEmpty()) 40f else ((sensorRatio / 100f) * 40f).coerceIn(30f, 40f)
        val earnedMaintenance = if (records.isEmpty()) 30f else ((docRatio / 100f) * 30f)
        val earnedCoaching = if (sessions.isEmpty()) 20f else 19.3f
        val earnedSchedule = 10.0f
        val calculatedTotalScore = Math.round((earnedIntegrity + earnedMaintenance + earnedCoaching + earnedSchedule) * 10f) / 10f

        val criteriaList = listOf(
            EvaluationCriteriaItem(
                category = "데이터 수집 무결성 (CAN 버스 직접 수집)",
                weightScore = 40,
                earnedScore = earnedIntegrity,
                detail = "임의 추정 없는 CAN 센서 계기판 차속 및 누적거리 직접 연동률 ${String.format("%.1f", sensorRatio)}% (터널 음영 정직 공시)",
                statusBadge = if (sensorRatio >= 95f) "최고 등급" else "우수"
            ),
            EvaluationCriteriaItem(
                category = "공식 정비 서류 증빙률 (영수증 대조)",
                weightScore = 30,
                earnedScore = earnedMaintenance,
                detail = "블루핸즈 공식 발행 정비명세서 원본 및 부품 번호 교차 검증 ${String.format("%.1f", docRatio)}% 완료",
                statusBadge = if (docRatio >= 95f) "완전 증빙" else "부분 증빙"
            ),
            EvaluationCriteriaItem(
                category = "주행 개선 코칭 및 운전 패턴 분석",
                weightScore = 20,
                earnedScore = earnedCoaching,
                detail = "동일 출퇴근 코스 비교 검증 주행 기반 탄력 주행 실천 (+1.3 km/L 연비 개선 견인)",
                statusBadge = "우수 실천"
            ),
            EvaluationCriteriaItem(
                category = "제조사 공식 소모품 교체 주기 준수",
                weightScore = 10,
                earnedScore = earnedSchedule,
                detail = "현대차 공식 취급설명서 정기점검표 기준 통상·가혹 조건 맞춤 소모품 권장 주기 내 관리",
                statusBadge = "적기 준수"
            )
        )

        val displayPlate = if (isMaskPlate) {
            val raw = profile.plateNumber
            if (raw.length > 4) raw.take(raw.length - 4) + "****" else "348두 ****"
        } else {
            profile.plateNumber
        }

        return TransparencyReport(
            vehicleName = profile.modelName,
            plateNumber = displayPlate,
            trackingPeriod = if (sessions.isEmpty()) "출고 초기 (데이터 누적 중)" else "2025.10 ~ 2026.11 (14개월)",
            totalTrackedKm = totalTrackedKm,
            sensorCollectionRatio = if (sessions.isEmpty()) 100f else sensorRatio,
            missingOrGapRatio = if (sessions.isEmpty()) 0f else (100f - sensorRatio),
            documentVerifiedMaintenanceRatio = if (records.isEmpty()) 100f else docRatio,
            totalTripsCount = sessions.size,
            comparableTripsCount = comparableCount,
            pendingJudgmentTripsCount = pendingCount,
            activeCoachingResult = if (sessions.isEmpty()) "첫 주행 대기 중 (실천 미션 준비)" else "${activeGoal?.title ?: "주행 코칭"} (${activeGoal?.improvementSummary ?: "진행 중"})",
            transparencyGrade = if (sessions.isEmpty()) "신차 준비 중" else if (calculatedTotalScore >= 95f) "AAA 등급 (최고 신뢰)" else "AA 등급",
            trustScore = if (sessions.isEmpty()) 100f else calculatedTotalScore,
            estimatedValueBonusWon = if (sessions.isEmpty()) 0 else 1100000,
            residualValueDefenseRate = if (sessions.isEmpty()) 0f else 3.8f,
            certificationNote = certNote,
            isMaskPlate = isMaskPlate,
            isMaskRouteLocation = isMaskRoute,
            powerTrainType = profile.powerTrainType,
            evaluationCriteria = criteriaList
        )
    }

    fun applyDemoScenario(scenario: DemoScenarioType) {
        _currentScenario.value = scenario
        when (scenario) {
            DemoScenarioType.STANDARD_VERIFIED -> {
                _vehicleProfile.value = getStandardVehicleProfile()
                _drivingSessions.value = getStandardDrivingSessions()
                _coachingGoals.value = getStandardCoachingGoals()
                _maintenanceSchedules.value = getStandardMaintenanceSchedules(UsageCondition.NORMAL)
                _maintenanceRecords.value = getStandardMaintenanceRecords()
            }
            DemoScenarioType.IONIQ5_EV -> {
                _vehicleProfile.value = VehicleProfile(
                    modelName = "아이오닉 5 롱레인지 2WD",
                    plateNumber = "24서 9876",
                    modelYear = 2025,
                    currentMileageKm = 18200,
                    powerTrainType = PowerTrainType.EV,
                    fuelType = "전기차 (배터리 EV)",
                    officialEfficiency = "5.2 km/kWh",
                    sensorConnectionStatus = "Pleos Connect EV 텔레매틱스 연동 중",
                    currentDriver = "김민준 (주 운전자 · 본인)",
                    usageCondition = UsageCondition.NORMAL,
                    batterySocPercent = 74,
                    batterySohPercent = 99,
                    lastSyncTime = "방금 전 (실시간)"
                )
                _drivingSessions.value = getEvDrivingSessions()
                _coachingGoals.value = getEvCoachingGoals()
                _maintenanceSchedules.value = getEvMaintenanceSchedules()
                _maintenanceRecords.value = getEvMaintenanceRecords()
            }
            DemoScenarioType.NEW_CAR_EMPTY -> {
                _vehicleProfile.value = VehicleProfile(
                    modelName = "디 올 뉴 코나 하이브리드",
                    plateNumber = "78너 1234",
                    modelYear = 2026,
                    currentMileageKm = 45,
                    powerTrainType = PowerTrainType.HYBRID,
                    fuelType = "하이브리드 (가솔린+전기)",
                    officialEfficiency = "19.8 km/L",
                    sensorConnectionStatus = "신차 Vehicle SDK 정상 활성화됨",
                    currentDriver = "김민준 (주 운전자 · 본인)",
                    usageCondition = UsageCondition.NORMAL
                )
                _drivingSessions.value = emptyList()
                _maintenanceRecords.value = emptyList()
                _maintenanceSchedules.value = listOf(
                    MaintenanceScheduleItem(
                        id = "sched_engine_oil",
                        name = "엔진오일 및 오일필터",
                        normalIntervalKm = 10000,
                        normalIntervalMonths = 12,
                        severeIntervalKm = 5000,
                        severeIntervalMonths = 6,
                        lastServicedKm = 0,
                        lastServicedDate = "출고 시점",
                        nextDueKm = 10000,
                        status = ScheduleStatus.NORMAL,
                        description = "현대차 공식 권장: 신차 첫 10,000 km 점검"
                    ),
                    MaintenanceScheduleItem(
                        id = "sched_cabin_filter",
                        name = "에어컨 / 히터 필터",
                        normalIntervalKm = 15000,
                        normalIntervalMonths = 12,
                        severeIntervalKm = 7500,
                        severeIntervalMonths = 6,
                        lastServicedKm = 0,
                        lastServicedDate = "출고 시점",
                        nextDueKm = 15000,
                        status = ScheduleStatus.NORMAL,
                        description = "신차 출고 필터 장착 (잔여 14,955 km)"
                    )
                )
                _coachingGoals.value = listOf(
                    CoachingGoal(
                        id = "goal_smooth_accel",
                        title = "신차 길들이기 & 부드러운 가속",
                        subtitle = "초기 1,000km 급가속 및 급제동 자제",
                        targetAction = "엔진 회전수 2,500 RPM 이하로 온화한 주행 유지",
                        rationale = "신차 엔진과 구동계의 원활한 마찰 정합을 위해 초기 길들이기를 제안합니다.",
                        isCurrentFocus = true,
                        progressCount = 0,
                        totalTargetTrips = 5,
                        beforeEfficiency = 19.8,
                        afterEfficiency = 19.8,
                        improvementSummary = "첫 주행 시 실천 여부를 확인합니다."
                    )
                )
            }
            DemoScenarioType.SEVERE_CONDITION_ALERT -> {
                _vehicleProfile.value = VehicleProfile(
                    id = "veh_sonata",
                    nickname = "1호차 (가혹 조건)",
                    modelName = "쏘나타 디 엣지 하이브리드",
                    plateNumber = "348두 7192",
                    modelYear = 2024,
                    currentMileageKm = 33800,
                    powerTrainType = PowerTrainType.HYBRID,
                    fuelType = "하이브리드 (가솔린+전기)",
                    officialEfficiency = "19.4 km/L",
                    sensorConnectionStatus = "차량 센서 정상 연동 중",
                    currentDriver = "김민준 (주 운전자)",
                    usageCondition = UsageCondition.SEVERE
                )
                _drivingSessions.value = getStandardDrivingSessions() + listOf(
                    DrivingSession(
                        id = 999L,
                        title = "폭설 및 빙판길 저속 주행 (조건 상이)",
                        dateFormatted = "2026.01.15 08:10",
                        distanceKm = 12.0,
                        durationMinutes = 60,
                        averageSpeedKmh = 12,
                        topSpeedKmh = 30,
                        efficiency = 11.2,
                        routeCategory = RouteCategory.URBAN,
                        isComparable = false,
                        isPendingJudgment = true,
                        pendingReason = "폭설 적설 도로 안전 서행으로 연비 왜곡 방지(판단 보류)",
                        dataSource = DataSourceType.VEHICLE_SDK,
                        dataQuality = DataQualityType.VERIFIED_COMPLETE,
                        rapidDecelCount = 4,
                        decelEvaluationNote = "빙판길 방어 운전 안전 제동 (감속 관측·원인 미확인)"
                    )
                )
                _maintenanceSchedules.value = listOf(
                    MaintenanceScheduleItem(
                        id = "sched_engine_oil",
                        name = "엔진오일 및 오일필터",
                        normalIntervalKm = 10000,
                        normalIntervalMonths = 12,
                        severeIntervalKm = 5000,
                        severeIntervalMonths = 6,
                        lastServicedKm = 28000,
                        lastServicedDate = "2026.04.10",
                        nextDueKm = 33000,
                        status = ScheduleStatus.OVERDUE,
                        description = "가혹 조건(5,000km 주기) 적용으로 800km 초과! 공식 점검 권장"
                    ),
                    MaintenanceScheduleItem(
                        id = "sched_cabin_filter",
                        name = "에어컨 / 히터 필터",
                        normalIntervalKm = 15000,
                        normalIntervalMonths = 12,
                        severeIntervalKm = 7500,
                        severeIntervalMonths = 6,
                        lastServicedKm = 27000,
                        lastServicedDate = "2026.03.10",
                        nextDueKm = 34500,
                        status = ScheduleStatus.APPROACHING,
                        description = "가혹 조건 적용 잔여 700 km (임박)"
                    )
                )
                _maintenanceRecords.value = getStandardMaintenanceRecords()
            }
        }
    }

    private fun getStandardVehicleProfile() = VehicleProfile(
        id = "veh_sonata",
        nickname = "1호차 (출퇴근 메인)",
        modelName = "쏘나타 디 엣지 하이브리드",
        plateNumber = "348두 7192",
        modelYear = 2024,
        currentMileageKm = 28450,
        powerTrainType = PowerTrainType.HYBRID,
        fuelType = "하이브리드 (가솔린+전기)",
        officialEfficiency = "19.4 km/L",
        sensorConnectionStatus = "차량 센서 정상 연동 중 (Vehicle SDK)",
        currentDriver = "김민준 (주 운전자 · 본인)",
        usageCondition = UsageCondition.NORMAL
    )

    private fun getStandardDrivingSessions(): List<DrivingSession> = listOf(
        DrivingSession(
            id = 105L,
            title = "월요일 출근길 고속화도로 주행",
            dateFormatted = "2026.11.02 08:20",
            distanceKm = 18.2,
            durationMinutes = 36,
            averageSpeedKmh = 30,
            topSpeedKmh = 72,
            efficiency = 21.4,
            routeCategory = RouteCategory.COMMUTE,
            isComparable = true,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 1,
            decelEvaluationNote = "신호 변경 구간 1회 안전 제동 확인 (감속 관측·원인 미확인)",
            coachingGoalMatched = "goal_smooth_accel",
            isCoachingGoalAchieved = true,
            speedSamples = listOf(0, 20, 35, 60, 40, 68, 55, 30, 0),
            efficiencySamples = listOf(14.0, 18.2, 21.0, 19.5, 20.4, 18.8, 22.1, 21.4, 0.0),
            driverName = "김민준 (주 운전자)",
            userSituationMemo = "앞차 간격 넉넉하게 유지하며 탄력 주행 실천",
            graphInsightSummary = "시속 60~72km 정속 구간에서 EV 모드 64% 개입 확인 → 공인연비 대비 +2.0 km/L 절감 달성"
        ),
        DrivingSession(
            id = 104L,
            title = "금요일 퇴근길 도심 정체 주행",
            dateFormatted = "2026.10.30 18:40",
            distanceKm = 19.1,
            durationMinutes = 45,
            averageSpeedKmh = 25,
            topSpeedKmh = 68,
            efficiency = 18.6,
            routeCategory = RouteCategory.COMMUTE,
            isComparable = true,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 2,
            decelEvaluationNote = "합류 차선 서행 제동 (임의 감점 없음)",
            coachingGoalMatched = "goal_smooth_accel",
            isCoachingGoalAchieved = true,
            speedSamples = listOf(0, 15, 25, 45, 30, 50, 40, 20, 0),
            efficiencySamples = listOf(13.5, 17.0, 19.2, 18.4, 19.0, 18.1, 19.5, 18.6, 0.0),
            driverName = "김민준 (주 운전자)",
            graphInsightSummary = "금요일 퇴근길 정체 시 탄력 주행 3.2km 지속으로 불필요한 급가속 0회 방어"
        ),
        DrivingSession(
            id = 103L,
            title = "국지성 폭우 우회 주행 (조건 상이)",
            dateFormatted = "2026.10.27 19:10",
            distanceKm = 23.4,
            durationMinutes = 68,
            averageSpeedKmh = 20,
            topSpeedKmh = 52,
            efficiency = 14.2,
            routeCategory = RouteCategory.COMMUTE,
            isComparable = false,
            isPendingJudgment = true,
            pendingReason = "시간당 40mm 폭우 및 침수 도로 우회로 조건 상이. AI 코치 개선 효과 판단 보류(Pending)",
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 3,
            decelEvaluationNote = "악천후 빗길 안전 서행 제동 확인 (감속 관측·원인 미확인)",
            speedSamples = listOf(0, 10, 20, 30, 15, 25, 10, 0),
            efficiencySamples = listOf(11.0, 13.5, 14.8, 13.2, 14.6, 14.2, 0.0),
            driverName = "김민준 (주 운전자)",
            userSituationMemo = "강남역 침수 우회로 진입으로 인해 저속 서행 운행함",
            graphInsightSummary = "기상 악화로 안전 운행 우선 → 비교 데이터 왜곡 방지를 위해 AI 코칭 판단 보류 처리"
        ),
        DrivingSession(
            id = 102L,
            title = "남산터널 경유 도심 주행 (음영 구간 발생)",
            dateFormatted = "2026.10.24 08:30",
            distanceKm = 16.5,
            durationMinutes = 34,
            averageSpeedKmh = 29,
            topSpeedKmh = 70,
            efficiency = 19.1,
            routeCategory = RouteCategory.URBAN,
            isComparable = true,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.PARTIAL_TUNNEL_GAP,
            rapidDecelCount = 1,
            decelEvaluationNote = "터널 진입 전 통상 감속 (정상 주행)",
            coachingGoalMatched = "goal_smooth_accel",
            isCoachingGoalAchieved = true,
            speedSamples = listOf(0, 25, 50, 60, 55, 65, 30, 0),
            efficiencySamples = listOf(14.2, 17.8, 20.1, 19.5, 18.9, 19.1, 0.0),
            driverName = "김민준 (주 운전자)",
            graphInsightSummary = "남산터널 GPS/속도 음영 3.2% 발생 구간 정직 공시 (임의 추정값 채우지 않음)"
        ),
        DrivingSession(
            id = 101L,
            title = "영동고속도로 장거리 항속 주행",
            dateFormatted = "2026.10.19 14:00",
            distanceKm = 118.0,
            durationMinutes = 88,
            averageSpeedKmh = 80,
            topSpeedKmh = 105,
            efficiency = 21.2,
            routeCategory = RouteCategory.HIGHWAY,
            isComparable = false,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 0,
            decelEvaluationNote = "고속도로 정속 주행 유지",
            speedSamples = listOf(0, 40, 80, 100, 95, 105, 90, 80, 0),
            efficiencySamples = listOf(16.0, 20.5, 22.1, 21.8, 21.0, 22.4, 21.2, 0.0),
            driverName = "이수진 (배우자)",
            graphInsightSummary = "영동고속도로 90~100km/h 스마트 크루즈 모드 88% 활용으로 공인연비 초과 달성"
        )
    )

    private fun getStandardCoachingGoals(): List<CoachingGoal> = listOf(
        CoachingGoal(
            id = "goal_smooth_accel",
            title = "교통 흐름 우선 출발 후 부드러운 가속",
            subtitle = "출근 상습 정체 전방 흐름 예측 및 급가속 줄이기",
            targetAction = "정체 서행 구간에서 2,000 RPM 이하 부드러운 가속과 조기 엑셀 오프",
            rationale = "반복 구간에서 속도 변동이 크면 연료 소모가 15% 이상 급증합니다. 안전 흐름을 우선하며 부드럽게 가속하는 과제를 제안합니다.",
            isCurrentFocus = true,
            progressCount = 3,
            totalTargetTrips = 4,
            beforeEfficiency = 18.2,
            afterEfficiency = 19.5,
            efficiencyUnit = "km/L",
            improvementSummary = "비교 가능 출근 4회 중 3회 성공! 연비 +1.3 km/L 향상 추세",
            applicableSituation = "출퇴근 반복 정체 구간",
            verificationMetric = "출퇴근 동일 경로 실질 연비 및 속도 분산도",
            pendingCondition = "폭우·침수 우회 등 도로 조건 상이 시 자동 보류"
        ),
        CoachingGoal(
            id = "goal_idle_stop",
            title = "유사 경로에서 불필요한 속도 변동 줄이기",
            subtitle = "차간 거리 예측을 통한 탄력 주행 및 관성 주행 확대",
            targetAction = "앞차와의 안전거리를 확보하고 불필요한 재가속 줄이기",
            rationale = "관성 주행 비율을 높이면 불필요한 연료 소모를 줄이고 브레이크 패드 마모를 방지합니다.",
            isCurrentFocus = false,
            progressCount = 0,
            totalTargetTrips = 5,
            beforeEfficiency = 17.5,
            afterEfficiency = 17.5,
            efficiencyUnit = "km/L",
            improvementSummary = "과제 선택 시 다음 주행부터 실천 여부를 추적합니다.",
            applicableSituation = "도심 시내 간선도로",
            verificationMetric = "급감속 횟수 및 관성 주행 비율",
            pendingCondition = "돌발 안전 제동 발생 시 감점 제외"
        ),
        CoachingGoal(
            id = "goal_highway_cruise",
            title = "주차 후 타이어 공기압 및 관리 일정 확인",
            subtitle = "적정 타이어 공기압(36psi) 유지로 연비 및 안전 확보",
            targetAction = "주행 종료 후 TPMS 공기압 확인 및 점검 일정 대조",
            rationale = "공기압 10% 저하 시 연비 2~3% 하락 및 타이어 편마모를 유발합니다.",
            isCurrentFocus = false,
            progressCount = 0,
            totalTargetTrips = 3,
            beforeEfficiency = 20.0,
            afterEfficiency = 20.0,
            efficiencyUnit = "km/L",
            improvementSummary = "과제 선택 시 주차 후 TPMS 상태로 확인합니다.",
            applicableSituation = "주차 후 차량 점검",
            verificationMetric = "TPMS 4륜 정상 압력 유지",
            pendingCondition = "센서 통신 결측 시 확인 보류"
        )
    )

    private fun getStandardMaintenanceSchedules(condition: UsageCondition): List<MaintenanceScheduleItem> = listOf(
        MaintenanceScheduleItem(
            id = "sched_engine_oil",
            name = "엔진오일 및 오일필터",
            normalIntervalKm = 10000,
            normalIntervalMonths = 12,
            severeIntervalKm = 5000,
            severeIntervalMonths = 6,
            lastServicedKm = 25000,
            lastServicedDate = "2026.08.14",
            nextDueKm = 35000,
            status = ScheduleStatus.NORMAL,
            description = "현대차 공식 권장: 통상 10,000 km, 가혹 5,000 km 또는 먼저 도래하는 시점"
        ),
        MaintenanceScheduleItem(
            id = "sched_cabin_filter",
            name = "에어컨 / 히터 필터",
            normalIntervalKm = 15000,
            normalIntervalMonths = 12,
            severeIntervalKm = 7500,
            severeIntervalMonths = 6,
            lastServicedKm = 15200,
            lastServicedDate = "2026.04.10",
            nextDueKm = 30200,
            status = ScheduleStatus.APPROACHING,
            description = "현재 28,450 km로 교체 잔여거리 1,750 km (임박)"
        ),
        MaintenanceScheduleItem(
            id = "sched_brake_pad",
            name = "브레이크 패드 및 오일 점검",
            normalIntervalKm = 40000,
            normalIntervalMonths = 24,
            severeIntervalKm = 20000,
            severeIntervalMonths = 12,
            lastServicedKm = 0,
            lastServicedDate = "출고 시점",
            nextDueKm = 40000,
            status = ScheduleStatus.NORMAL,
            description = "40,000 km 도달 시 공식 정비소 육안 및 두께 점검 권장"
        ),
        MaintenanceScheduleItem(
            id = "sched_tire_rotation",
            name = "타이어 위치 교환 및 공기압",
            normalIntervalKm = 10000,
            normalIntervalMonths = 6,
            severeIntervalKm = 5000,
            severeIntervalMonths = 3,
            lastServicedKm = 25000,
            lastServicedDate = "2026.08.14",
            nextDueKm = 35000,
            status = ScheduleStatus.NORMAL,
            description = "편마모 방지 및 트레드 수명 균일화"
        )
    )

    private fun getStandardMaintenanceRecords(): List<MaintenanceRecord> = listOf(
        MaintenanceRecord(
            id = 201L,
            dateFormatted = "2026.08.14",
            mileageKm = 25000,
            workshopName = "현대 블루핸즈 서초점",
            items = listOf("하이브리드 저점도 엔진오일(0W-16) 교체", "오일필터 & 에어크리너 어셈블리 교체", "타이어 4륜 공기압(36psi) 보충", "하부 섀시 부식 및 누유 점검"),
            actionType = MaintenanceActionType.REPLACEMENT,
            totalCostWon = 105000,
            isDocumentVerified = true,
            verificationLevel = VerificationLevel.DATA_MATCHED,
            documentTitle = "공식 정비명세서 (블루핸즈 발행)",
            receiptMemo = "영수증 및 작업 명세서 데이터 대조 완료 (주행거리 25,000 km 일치)",
            invoiceNumber = "제 2024-HMC-0842호",
            businessRegNumber = "214-85-19402",
            technicianName = "박진호 (국가공인 자동차정비기능장)",
            partsDetailList = listOf(
                MaintenancePartDetail("현대 순정 가솔린/하이브리드 엔진오일 (4L)", MaintenanceActionType.REPLACEMENT, 1, 52000, 25000),
                MaintenancePartDetail("엔진 오일필터 엘리먼트", MaintenanceActionType.REPLACEMENT, 1, 9500, 8500),
                MaintenancePartDetail("에어크리너 필터", MaintenanceActionType.REPLACEMENT, 1, 10000, 0),
                MaintenancePartDetail("4륜 타이어 규정 공기압 보충(36psi)", MaintenanceActionType.REPLENISHMENT, 4, 0, 0),
                MaintenancePartDetail("하부 누유 및 서스펜션 체결 상태 점검", MaintenanceActionType.INSPECTION, 1, 0, 0)
            )
        ),
        MaintenanceRecord(
            id = 200L,
            dateFormatted = "2026.04.10",
            mileageKm = 15200,
            workshopName = "현대 블루핸즈 역삼점",
            items = listOf("프리미엄 항균 에어컨/히터 필터 교체", "타이어 앞/뒤 위치 교환 (X자 교차)"),
            actionType = MaintenanceActionType.REPLACEMENT,
            totalCostWon = 62000,
            isDocumentVerified = true,
            verificationLevel = VerificationLevel.DATA_MATCHED,
            documentTitle = "정비 영수증 & 전자 명세서",
            receiptMemo = "카드 결제 영수증 및 부품 번호 대조 완료",
            invoiceNumber = "제 2024-HMC-0319호",
            businessRegNumber = "211-82-44109",
            technicianName = "최영민 (선임 테크니션)",
            partsDetailList = listOf(
                MaintenancePartDetail("순정 프리미엄 미세먼지 항균 캐빈필터", MaintenanceActionType.REPLACEMENT, 1, 38000, 12000),
                MaintenancePartDetail("타이어 4륜 위치 교환 및 휠 밸런스 조정", MaintenanceActionType.INSPECTION, 1, 0, 12000)
            )
        ),
        MaintenanceRecord(
            id = 199L,
            dateFormatted = "2025.10.05",
            mileageKm = 8500,
            workshopName = "현대 모터스튜디오 고양 서비스센터",
            items = listOf("1년차 신차 정기 기본 점검 (15개 공인 항목)", "브레이크 패드 잔량 측정 (전 9.2mm / 후 8.8mm)", "하이브리드 고전압 배터리 냉각수 점검"),
            actionType = MaintenanceActionType.INSPECTION,
            totalCostWon = 0,
            isDocumentVerified = true,
            verificationLevel = VerificationLevel.ISSUER_VERIFIED,
            documentTitle = "1년차 신차 정기점검 결과서",
            receiptMemo = "현대차 공식 전산 보증점검 문서 연동 확인",
            invoiceNumber = "제 2023-HMS-1002호",
            businessRegNumber = "128-85-02100",
            technicianName = "정재훈 (공식 수석 하이테크 마스터)",
            partsDetailList = listOf(
                MaintenancePartDetail("신차 출고 1년차 15대 안전항목 전산 진단", MaintenanceActionType.INSPECTION, 1, 0, 0),
                MaintenancePartDetail("브레이크 디스크 및 패드 마모도 정밀 측정", MaintenanceActionType.INSPECTION, 1, 0, 0),
                MaintenancePartDetail("고전압 배터리 제어기(BMS) DTC 무결성 진단", MaintenanceActionType.INSPECTION, 1, 0, 0)
            )
        )
    )

    // EV specific presets
    private fun getEvDrivingSessions(): List<DrivingSession> = listOf(
        DrivingSession(
            id = 301L,
            title = "판교 출근길 전비 주행 (반복 경로)",
            dateFormatted = "2026.11.09 08:30",
            distanceKm = 22.4,
            durationMinutes = 40,
            averageSpeedKmh = 34,
            topSpeedKmh = 75,
            efficiency = 6.4,
            efficiencyUnit = "km/kWh",
            routeCategory = RouteCategory.COMMUTE,
            isComparable = true,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 0,
            decelEvaluationNote = "회생제동 레벨2 스마트 감속 완료 (소비 전력 3.5 kWh)",
            coachingGoalMatched = "goal_ev_regen",
            isCoachingGoalAchieved = true,
            speedSamples = listOf(0, 20, 45, 60, 50, 70, 55, 30, 0),
            efficiencySamples = listOf(4.5, 5.8, 6.5, 6.2, 6.8, 6.1, 6.4, 0.0),
            energyConsumedKwh = 3.5,
            userSituationMemo = "i-Pedal 원페달 모드로 회생제동 에너지 회수 극대화"
        ),
        DrivingSession(
            id = 302L,
            title = "강남 도심 정체 주행",
            dateFormatted = "2026.11.06 18:50",
            distanceKm = 14.8,
            durationMinutes = 48,
            averageSpeedKmh = 19,
            topSpeedKmh = 50,
            efficiency = 5.8,
            efficiencyUnit = "km/kWh",
            routeCategory = RouteCategory.URBAN,
            isComparable = true,
            isPendingJudgment = false,
            dataSource = DataSourceType.VEHICLE_SDK,
            dataQuality = DataQualityType.VERIFIED_COMPLETE,
            rapidDecelCount = 1,
            decelEvaluationNote = "신호 대기 공조 전력 소비 반영 (소비 전력 2.5 kWh)",
            coachingGoalMatched = "goal_ev_regen",
            isCoachingGoalAchieved = false,
            speedSamples = listOf(0, 15, 25, 40, 20, 35, 10, 0),
            efficiencySamples = listOf(4.2, 5.0, 5.9, 5.6, 6.0, 5.8, 0.0),
            energyConsumedKwh = 2.5
        )
    )

    private fun getEvCoachingGoals(): List<CoachingGoal> = listOf(
        CoachingGoal(
            id = "goal_ev_regen",
            title = "회생제동 활용 탄력 감속 및 에너지 회수",
            subtitle = "원페달 또는 패들 시프트 회생제동 단계 최적화",
            targetAction = "정차 예상 시 풋브레이크 급조작 대신 회생제동을 점진적으로 활용",
            rationale = "회생제동 효율을 20% 높이면 도심 주행 가능 거리가 배터리 잔량 대비 10% 이상 증가합니다.",
            isCurrentFocus = true,
            progressCount = 2,
            totalTargetTrips = 3,
            beforeEfficiency = 5.6,
            afterEfficiency = 6.2,
            efficiencyUnit = "km/kWh",
            improvementSummary = "비교 가능 출근 3회 중 2회 성공! 전비 +0.6 km/kWh 향상 추세",
            applicableSituation = "반복 출퇴근 감속 구간",
            verificationMetric = "회생제동 에너지 회수율(kWh) 및 전비",
            pendingCondition = "영하 10도 이하 배터리 저온 시 자동 보류"
        )
    )

    private fun getEvMaintenanceSchedules(): List<MaintenanceScheduleItem> = listOf(
        MaintenanceScheduleItem(
            id = "sched_ev_coolant",
            name = "전기차 절연형 저전도 냉각수",
            normalIntervalKm = 60000,
            normalIntervalMonths = 36,
            severeIntervalKm = 40000,
            severeIntervalMonths = 24,
            lastServicedKm = 0,
            lastServicedDate = "출고 시점",
            nextDueKm = 60000,
            status = ScheduleStatus.NORMAL,
            description = "고전압 배터리 및 모터 열관리 전용 절연 냉각수 점검"
        ),
        MaintenanceScheduleItem(
            id = "sched_ev_reducer_oil",
            name = "EV 감속기 오일 점검",
            normalIntervalKm = 60000,
            normalIntervalMonths = 48,
            severeIntervalKm = 30000,
            severeIntervalMonths = 24,
            lastServicedKm = 0,
            lastServicedDate = "출고 시점",
            nextDueKm = 60000,
            status = ScheduleStatus.NORMAL,
            description = "전기 모터 동력 전달 감속기 기어 오일 누유 및 점검"
        ),
        MaintenanceScheduleItem(
            id = "sched_cabin_filter",
            name = "에어컨 / 히터 항균 필터",
            normalIntervalKm = 15000,
            normalIntervalMonths = 12,
            severeIntervalKm = 7500,
            severeIntervalMonths = 6,
            lastServicedKm = 12000,
            lastServicedDate = "2026.06.20",
            nextDueKm = 27000,
            status = ScheduleStatus.NORMAL,
            description = "실내 공기 정화 필터 (잔여 8,800 km)"
        )
    )

    private fun getEvMaintenanceRecords(): List<MaintenanceRecord> = listOf(
        MaintenanceRecord(
            id = 401L,
            dateFormatted = "2026.06.20",
            mileageKm = 12000,
            workshopName = "현대 하이테크센터 동부서비스",
            items = listOf("EV 고전압 배터리 시스템(BMS) 진단", "에어컨 필터 교체", "고전압 절연저항 점검"),
            actionType = MaintenanceActionType.REPLACEMENT,
            totalCostWon = 48000,
            isDocumentVerified = true,
            verificationLevel = VerificationLevel.ISSUER_VERIFIED,
            documentTitle = "현대 EV 특화 공식 점검 내역서",
            receiptMemo = "고전압 배터리 SOH 99% 정상 확인 및 펌웨어 최신 업데이트"
        )
    )
}
