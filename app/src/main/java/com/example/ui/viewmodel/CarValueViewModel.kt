package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.CarValueRepository
import com.example.data.repository.DemoScenarioType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppTab(val label: String) {
    HOME("내 차"),
    TRIPS_COACHING("주행 코치"),
    MAINTENANCE("정비 관리"),
    REPORT("이력 리포트")
}

data class CarValueUiState(
    val currentTab: AppTab = AppTab.HOME,
    val tripFilter: String = "ALL", // ALL, COMPARABLE, PENDING
    val selectedTripDetail: DrivingSession? = null,
    val selectedMaintenanceDetail: MaintenanceRecord? = null,
    val showRegisterMaintenanceModal: Boolean = false,
    val showLiveDriveModal: Boolean = false,
    val showReportShareModal: Boolean = false,
    val showTelemetryModal: Boolean = false,
    val showDemoScenarioModal: Boolean = false,
    val showVehicleDetailModal: Boolean = false,
    val showGarageModal: Boolean = false,
    val showAddVehicleDialog: Boolean = false,
    val showOfficialManualDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val showGradeGuidelineDialog: Boolean = false,
    val showAppGuideDialog: Boolean = false,
    val maskPlateNumber: Boolean = false,
    val maskRouteLocation: Boolean = false,
    val feedbackMessage: String? = null,
    // Auth / Hyundai Login State
    val isLoggedIn: Boolean = false,
    val isAuthenticating: Boolean = false,
    val loggedInUserName: String = "김민준",
    val loggedInUserId: String = "minjun.kim@hyundai.com",
    val loginError: String? = null,
    // Preferences
    val appSettings: AppSettings = AppSettings(),
    // Drive Simulator State
    val isSimulatingDrive: Boolean = false,
    val simCurrentSpeedKmh: Int = 0,
    val simDistanceKm: Double = 0.0,
    val simEfficiencyKml: Double = 19.2,
    val simDurationSec: Int = 0,
    val simDriveFinishedSession: DrivingSession? = null
)

class CarValueViewModel(
    private val repository: CarValueRepository = CarValueRepository()
) : ViewModel() {

    val vehicleProfile = repository.vehicleProfile
    val garageVehicles = repository.garageVehicles
    val registeredDrivers = repository.registeredDrivers
    val drivingSessions = repository.drivingSessions
    val coachingGoals = repository.coachingGoals
    val maintenanceSchedules = repository.maintenanceSchedules
    val maintenanceRecords = repository.maintenanceRecords
    val currentScenario = repository.currentScenario

    private val _uiState = MutableStateFlow(CarValueUiState())
    val uiState: StateFlow<CarValueUiState> = _uiState.asStateFlow()

    private var driveSimulationJob: Job? = null

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setTripFilter(filter: String) {
        _uiState.update { it.copy(tripFilter = filter) }
    }

    fun openTripDetail(session: DrivingSession) {
        _uiState.update { it.copy(selectedTripDetail = session) }
    }

    fun closeTripDetail() {
        _uiState.update { it.copy(selectedTripDetail = null) }
    }

    fun openMaintenanceDetail(record: MaintenanceRecord) {
        _uiState.update { it.copy(selectedMaintenanceDetail = record) }
    }

    fun closeMaintenanceDetail() {
        _uiState.update { it.copy(selectedMaintenanceDetail = null) }
    }

    fun setShowRegisterMaintenance(show: Boolean) {
        _uiState.update { it.copy(showRegisterMaintenanceModal = show) }
    }

    fun setShowLiveDriveModal(show: Boolean) {
        if (!show && _uiState.value.isSimulatingDrive) {
            stopDriveSimulation()
        }
        _uiState.update {
            it.copy(
                showLiveDriveModal = show,
                simDriveFinishedSession = if (!show) null else it.simDriveFinishedSession
            )
        }
    }

    fun dismissDriveModal() {
        if (_uiState.value.isSimulatingDrive) {
            stopDriveSimulation()
        }
        _uiState.update {
            it.copy(
                showLiveDriveModal = false,
                simDriveFinishedSession = null,
                isSimulatingDrive = false
            )
        }
    }

    fun selectGarageVehicle(vehicleId: String) {
        repository.selectGarageVehicle(vehicleId)
        val selected = repository.vehicleProfile.value
        setFeedbackMessage("'${selected.nickname} - ${selected.modelName}'(으)로 전환되었습니다.")
    }

    fun addGarageVehicle(nickname: String, modelName: String, plateNumber: String, powerTrain: PowerTrainType, driver: String) {
        repository.addGarageVehicle(nickname, modelName, plateNumber, powerTrain, driver)
        setFeedbackMessage("새 차량 '${modelName} (${plateNumber})'이 차고에 등록되었습니다.")
    }

    fun setShowGarageModal(show: Boolean) {
        _uiState.update { it.copy(showGarageModal = show) }
    }

    fun setShowAddVehicleDialog(show: Boolean) {
        _uiState.update { it.copy(showAddVehicleDialog = show) }
    }

    fun setShowOfficialManualDialog(show: Boolean) {
        _uiState.update { it.copy(showOfficialManualDialog = show) }
    }

    fun setShowSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showSettingsDialog = show) }
    }

    fun setShowGradeGuidelineDialog(show: Boolean) {
        _uiState.update { it.copy(showGradeGuidelineDialog = show) }
    }

    fun setShowAppGuideDialog(show: Boolean) {
        _uiState.update { it.copy(showAppGuideDialog = show) }
    }

    fun setShowReportShareModal(show: Boolean) {
        _uiState.update { it.copy(showReportShareModal = show) }
    }

    fun setShowTelemetryModal(show: Boolean) {
        _uiState.update { it.copy(showTelemetryModal = show) }
    }

    fun setShowDemoScenarioModal(show: Boolean) {
        _uiState.update { it.copy(showDemoScenarioModal = show) }
    }

    fun setShowVehicleDetailModal(show: Boolean) {
        _uiState.update { it.copy(showVehicleDetailModal = show) }
    }

    fun toggleMaskPlate() {
        _uiState.update { it.copy(maskPlateNumber = !it.maskPlateNumber) }
    }

    fun toggleMaskRoute() {
        _uiState.update { it.copy(maskRouteLocation = !it.maskRouteLocation) }
    }

    fun updateUsageCondition(condition: UsageCondition) {
        repository.updateUsageCondition(condition)
        setFeedbackMessage("${condition.label}이 적용되었습니다 (정기점검 주기 동적 갱신).")
    }

    fun updateCurrentDriver(driver: String) {
        repository.updateCurrentDriver(driver)
        setFeedbackMessage("운전자가 '${driver}'(으)로 변경되었습니다.")
    }

    fun addCustomDriver(driver: String) {
        val trimmed = driver.trim()
        if (trimmed.isEmpty()) return
        repository.addCustomDriver(trimmed)
        setFeedbackMessage("새 운전자 '${trimmed}'이(가) 등록 및 선택되었습니다.")
    }

    fun removeCustomDriver(driver: String) {
        repository.removeCustomDriver(driver)
        setFeedbackMessage("운전자 '${driver}'이(가) 목록에서 제외되었습니다.")
    }

    fun initSettings(settings: AppSettings) {
        _uiState.update {
            it.copy(
                appSettings = settings,
                maskPlateNumber = settings.defaultPrivacyMasking
            )
        }
    }

    fun updateSettings(settings: AppSettings) {
        _uiState.update {
            it.copy(
                appSettings = settings,
                maskPlateNumber = settings.defaultPrivacyMasking
            )
        }
    }

    fun loginWithHyundaiAccount(userId: String, password: String) {
        val trimmedId = userId.trim()
        if (trimmedId.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(loginError = "현대자동차 계정 아이디와 비밀번호를 입력해주세요.") }
            return
        }
        if (password.length < 4) {
            _uiState.update { it.copy(loginError = "비밀번호는 4자 이상이어야 합니다.") }
            return
        }
        _uiState.update { it.copy(isAuthenticating = true, loginError = null) }
        viewModelScope.launch {
            delay(550)
            val displayName = if (trimmedId.contains("minjun", ignoreCase = true) || trimmedId == "kimminjun") {
                "김민준"
            } else {
                trimmedId.substringBefore("@").ifBlank { "김민준" }
            }
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isAuthenticating = false,
                    loggedInUserName = displayName,
                    loggedInUserId = trimmedId,
                    loginError = null,
                    feedbackMessage = "${displayName}님, 현대자동차 계정 로그인 완료"
                )
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                isAuthenticating = false,
                showSettingsDialog = false,
                currentTab = AppTab.HOME,
                loginError = null
            )
        }
    }

    fun saveTripSituationMemo(tripId: Long, memo: String) {
        repository.saveTripSituationMemo(tripId, memo)
        _uiState.update { state ->
            val updatedDetail = if (state.selectedTripDetail?.id == tripId) {
                state.selectedTripDetail.copy(userSituationMemo = memo)
            } else state.selectedTripDetail
            state.copy(
                selectedTripDetail = updatedDetail,
                feedbackMessage = "운전자 상황 메모가 저장되었습니다."
            )
        }
    }

    fun setFeedbackMessage(message: String) {
        _uiState.update { it.copy(feedbackMessage = message) }
    }

    fun clearFeedbackMessage() {
        _uiState.update { it.copy(feedbackMessage = null) }
    }

    fun applyDemoScenario(scenario: DemoScenarioType) {
        repository.applyDemoScenario(scenario)
        _uiState.update {
            it.copy(
                showDemoScenarioModal = false,
                feedbackMessage = "'${scenario.title}' 시나리오가 적용되었습니다."
            )
        }
    }

    fun selectCoachingGoal(goalId: String) {
        repository.selectCoachingGoal(goalId)
        val selected = repository.coachingGoals.value.firstOrNull { it.id == goalId }
        setFeedbackMessage("AI 실천 과제가 '${selected?.title ?: "새 과제"}'(으)로 설정되었습니다.")
    }

    fun startDriveSimulation() {
        val isEv = vehicleProfile.value.powerTrainType == PowerTrainType.EV
        val initialEff = if (isEv) 6.0 else 19.0
        _uiState.update {
            it.copy(
                isSimulatingDrive = true,
                simCurrentSpeedKmh = 35,
                simDistanceKm = 0.1,
                simEfficiencyKml = initialEff,
                simDurationSec = 0,
                simDriveFinishedSession = null
            )
        }
        driveSimulationJob?.cancel()
        driveSimulationJob = viewModelScope.launch {
            while (_uiState.value.isSimulatingDrive) {
                delay(1000)
                _uiState.update { state ->
                    val nextSec = state.simDurationSec + 1
                    val speedVariation = Random.nextInt(38, 72)
                    val nextDist = state.simDistanceKm + (speedVariation / 3600.0)
                    val baseEff = if (isEv) 6.2 else 19.4
                    val nextEfficiency = (baseEff + Random.nextDouble(-0.3, 0.4)).coerceIn(if (isEv) 4.5 else 16.5, if (isEv) 7.8 else 22.8)
                    state.copy(
                        simDurationSec = nextSec,
                        simCurrentSpeedKmh = speedVariation,
                        simDistanceKm = (Math.round(nextDist * 100.0) / 100.0),
                        simEfficiencyKml = (Math.round(nextEfficiency * 10.0) / 10.0)
                    )
                }
            }
        }
    }

    fun stopDriveSimulation() {
        driveSimulationJob?.cancel()
        val currentDist = _uiState.value.simDistanceKm.coerceAtLeast(1.8)
        val currentDur = (_uiState.value.simDurationSec / 60).coerceAtLeast(4)
        val efficiency = _uiState.value.simEfficiencyKml
        val avgSpeed = (_uiState.value.simCurrentSpeedKmh).coerceIn(30, 60)
        val isEv = vehicleProfile.value.powerTrainType == PowerTrainType.EV
        val effUnit = vehicleProfile.value.powerTrainType.efficiencyUnit

        repository.addDrivingSession(
            title = if (isEv) "방금 완료된 EV 전비 주행 (실시간 기록)" else "방금 완료된 주행 (실시간 기록)",
            distanceKm = (Math.round(currentDist * 10.0) / 10.0),
            durationMinutes = currentDur,
            averageSpeedKmh = avgSpeed,
            topSpeedKmh = (avgSpeed + 18),
            efficiency = efficiency,
            routeCategory = RouteCategory.COMMUTE,
            isComparable = true,
            isPending = false
        )

        val latestSession = repository.drivingSessions.value.first()

        _uiState.update {
            it.copy(
                isSimulatingDrive = false,
                simDriveFinishedSession = latestSession,
                feedbackMessage = "새로운 주행 세션이 저장되었습니다 (효율 ${efficiency} $effUnit)"
            )
        }
    }

    fun registerMaintenance(
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
        repository.addMaintenanceRecord(
            workshopName = workshopName,
            mileageKm = mileageKm,
            items = items,
            actionType = actionType,
            totalCostWon = totalCostWon,
            isDocumentVerified = isDocumentVerified,
            verificationLevel = verificationLevel,
            documentTitle = documentTitle,
            receiptMemo = receiptMemo
        )
        _uiState.update {
            it.copy(
                showRegisterMaintenanceModal = false,
                feedbackMessage = "정비 내역이 등록되었습니다. 소모품 관리 주기와 리포트에 즉시 반영되었습니다."
            )
        }
    }

    fun getTransparencyReport(): TransparencyReport {
        return repository.getTransparencyReport(
            isMaskPlate = _uiState.value.maskPlateNumber,
            isMaskRoute = _uiState.value.maskRouteLocation
        )
    }
}
