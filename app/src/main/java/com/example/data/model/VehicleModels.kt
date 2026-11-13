package com.example.data.model

enum class PowerTrainType(val label: String, val efficiencyUnit: String) {
    HYBRID("하이브리드 (가솔린+전기)", "km/L"),
    EV("전기차 (배터리 EV)", "km/kWh"),
    GASOLINE("가솔린 내연기관", "km/L")
}

enum class UsageCondition(val label: String, val description: String) {
    NORMAL("통상 조건", "제조사 표준 권장 주기 적용"),
    SEVERE("가혹 조건", "단거리 반복, 상습 정체, 험로 주행 시 주기 50% 단축")
}

enum class RouteCategory(val label: String) {
    COMMUTE("반복 출퇴근"),
    HIGHWAY("고속화도로"),
    URBAN("도심 시내"),
    GENERAL("일반 주행")
}

enum class DataSourceType(val label: String, val isSensorBacked: Boolean) {
    VEHICLE_SDK("차량 센서 (Pleos Connect Vehicle SDK)", true),
    MANUAL_INPUT("사용자 수기 입력", false)
}

enum class DataQualityType(val label: String, val isComplete: Boolean) {
    VERIFIED_COMPLETE("정상 수집 (100% 무결성)", true),
    PARTIAL_TUNNEL_GAP("부분 누락 (터널 GPS/속도 미확인 구간 3.2% 발생)", false),
    MANUAL_INPUT("외부 수기 등록 (센서 비연동)", false)
}

enum class VerificationLevel(val label: String) {
    USER_INPUT("사용자 입력"),
    DOCUMENT_ATTACHED("문서 첨부"),
    DATA_MATCHED("항목 대조 완료"),
    ISSUER_VERIFIED("발급처 확인 (시연)")
}

enum class MaintenanceActionType(val label: String) {
    INSPECTION("점검"),
    REPLENISHMENT("보충"),
    REPLACEMENT("교체")
}

enum class ScheduleStatus(val label: String) {
    NORMAL("권장 주기 이내"),
    APPROACHING("교체 권장 임박"),
    OVERDUE("점검 필요")
}

data class VehicleProfile(
    val id: String = "veh_sonata",
    val nickname: String = "1호차 (출퇴근 메인)",
    val modelName: String = "쏘나타 디 엣지 하이브리드",
    val plateNumber: String = "348두 7192",
    val modelYear: Int = 2024,
    val currentMileageKm: Int = 28450,
    val powerTrainType: PowerTrainType = PowerTrainType.HYBRID,
    val fuelType: String = "하이브리드 (가솔린+전기)",
    val officialEfficiency: String = "19.4 km/L",
    val sensorConnectionStatus: String = "정상 연동 중 (Vehicle SDK)",
    val currentDriver: String = "김민준 (주 운전자 · 본인)",
    val usageCondition: UsageCondition = UsageCondition.NORMAL,
    val batterySocPercent: Int? = null, // EV용 충전 잔량 (%)
    val batterySohPercent: Int? = null, // EV용 배터리 건강도 SOH (%)
    val lastSyncTime: String = "방금 전 (실시간 연동)"
)

data class DrivingSession(
    val id: Long,
    val title: String,
    val dateFormatted: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val averageSpeedKmh: Int,
    val topSpeedKmh: Int,
    val efficiency: Double,
    val efficiencyUnit: String = "km/L",
    val routeCategory: RouteCategory,
    val isComparable: Boolean, // 반복 출퇴근 등 비교 가능 주행
    val isPendingJudgment: Boolean = false, // 기상 악화, 폭우, 극심한 우회 등으로 판단 보류
    val pendingReason: String? = null,
    val dataSource: DataSourceType = DataSourceType.VEHICLE_SDK,
    val dataQuality: DataQualityType = DataQualityType.VERIFIED_COMPLETE,
    val rapidDecelCount: Int = 0,
    val decelEvaluationNote: String = "급감속을 무조건적인 운전 결함으로 판단하지 않고 도로 안전 흐름으로 분석",
    val coachingGoalMatched: String? = null,
    val isCoachingGoalAchieved: Boolean? = null,
    val speedSamples: List<Int> = listOf(0, 25, 45, 60, 55, 70, 65, 30, 0),
    val efficiencySamples: List<Double> = listOf(14.0, 18.2, 21.0, 19.5, 20.4, 18.8, 22.1, 19.8, 0.0),
    val energyConsumedKwh: Double? = null, // EV용 소비 전력
    val driverName: String = "김민준 (주 운전자 · 본인)",
    val userSituationMemo: String? = null, // 운전자 상황 설명 메모
    val graphInsightSummary: String? = null // 데이터 도출 인사이트 및 분석 요약
)

data class CoachingGoal(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetAction: String,
    val rationale: String,
    val isCurrentFocus: Boolean,
    val progressCount: Int,
    val totalTargetTrips: Int,
    val beforeEfficiency: Double,
    val afterEfficiency: Double,
    val efficiencyUnit: String = "km/L",
    val improvementSummary: String,
    val applicableSituation: String = "출퇴근 반복 정체 구간",
    val verificationMetric: String = "탄력 주행 유지율 및 실질 연비 변화",
    val pendingCondition: String = "폭우·폭설 기상 악화 시 자동 판단 보류"
)

data class MaintenancePartDetail(
    val partName: String,
    val actionType: MaintenanceActionType,
    val quantity: Int = 1,
    val partCost: Int,
    val laborCost: Int
) {
    val totalCost: Int get() = partCost + laborCost
}

data class MaintenanceRecord(
    val id: Long,
    val dateFormatted: String,
    val mileageKm: Int,
    val workshopName: String,
    val items: List<String>,
    val actionType: MaintenanceActionType = MaintenanceActionType.REPLACEMENT,
    val totalCostWon: Int,
    val isDocumentVerified: Boolean,
    val verificationLevel: VerificationLevel = VerificationLevel.DATA_MATCHED,
    val documentTitle: String,
    val receiptMemo: String,
    val invoiceNumber: String = "제 2024-HMC-0842호",
    val businessRegNumber: String = "214-85-19402",
    val technicianName: String = "박진호 (국가공인 자동차정비기능장)",
    val partsDetailList: List<MaintenancePartDetail> = emptyList()
)

data class MaintenanceScheduleItem(
    val id: String,
    val name: String,
    val normalIntervalKm: Int,
    val normalIntervalMonths: Int,
    val severeIntervalKm: Int,
    val severeIntervalMonths: Int,
    val lastServicedKm: Int,
    val lastServicedDate: String,
    val nextDueKm: Int,
    val status: ScheduleStatus,
    val description: String,
    val officialManualSource: String = "현대차 공식 취급설명서 정기점검표"
) {
    val officialIntervalKm: Int get() = normalIntervalKm
    val officialIntervalMonths: Int get() = normalIntervalMonths
}

data class EvaluationCriteriaItem(
    val category: String,
    val weightScore: Int,
    val earnedScore: Float,
    val detail: String,
    val statusBadge: String
)

data class TransparencyReport(
    val vehicleName: String,
    val plateNumber: String,
    val trackingPeriod: String,
    val totalTrackedKm: Int,
    val sensorCollectionRatio: Float, // e.g. 96.8%
    val missingOrGapRatio: Float, // e.g. 3.2%
    val documentVerifiedMaintenanceRatio: Float, // e.g. 100%
    val totalTripsCount: Int,
    val comparableTripsCount: Int,
    val pendingJudgmentTripsCount: Int,
    val activeCoachingResult: String,
    val transparencyGrade: String = "AAA 등급",
    val trustScore: Float = 98.5f, // 100점 만점 환산 점수
    val estimatedValueBonusWon: Int = 1100000, // 중고차 시장 가치 환산금액
    val residualValueDefenseRate: Float = 3.8f, // 잔존가치 방어율 (+3.8%p)
    val certificationNote: String,
    val isMaskPlate: Boolean = false,
    val isMaskRouteLocation: Boolean = false,
    val powerTrainType: PowerTrainType = PowerTrainType.HYBRID,
    val evaluationCriteria: List<EvaluationCriteriaItem> = emptyList()
)

data class AppSettings(
    val isDarkMode: Boolean = false,
    val autoDetectDrive: Boolean = true,
    val coachingAlertsEnabled: Boolean = true,
    val maintenanceAlertThresholdKm: Int = 2000,
    val defaultPrivacyMasking: Boolean = false,
    val showDetailAxisLabels: Boolean = true
)

