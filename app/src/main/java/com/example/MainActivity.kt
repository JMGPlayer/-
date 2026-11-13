package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppSettings
import com.example.ui.dialogs.*
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MaintenanceScreen
import com.example.ui.screens.TripsCoachingScreen
import com.example.ui.screens.ValueReportScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.CarValueViewModel

private const val PREFS_NAME = "car_value_coach_prefs"
private const val KEY_DARK_MODE = "is_dark_mode"
private const val KEY_AUTO_DETECT_DRIVE = "auto_detect_drive"
private const val KEY_COACHING_ALERTS = "coaching_alerts"
private const val KEY_MAINT_THRESHOLD_KM = "maint_threshold_km"
private const val KEY_PRIVACY_MASKING = "default_privacy_masking"
private const val KEY_DETAIL_AXIS_LABELS = "show_detail_axis_labels"

private fun loadAppSettings(context: Context): AppSettings {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return AppSettings(
        isDarkMode = prefs.getBoolean(KEY_DARK_MODE, false),
        autoDetectDrive = prefs.getBoolean(KEY_AUTO_DETECT_DRIVE, true),
        coachingAlertsEnabled = prefs.getBoolean(KEY_COACHING_ALERTS, true),
        maintenanceAlertThresholdKm = prefs.getInt(KEY_MAINT_THRESHOLD_KM, 2000),
        defaultPrivacyMasking = prefs.getBoolean(KEY_PRIVACY_MASKING, false),
        showDetailAxisLabels = prefs.getBoolean(KEY_DETAIL_AXIS_LABELS, true)
    )
}

private fun saveAppSettings(context: Context, settings: AppSettings) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(KEY_DARK_MODE, settings.isDarkMode)
        .putBoolean(KEY_AUTO_DETECT_DRIVE, settings.autoDetectDrive)
        .putBoolean(KEY_COACHING_ALERTS, settings.coachingAlertsEnabled)
        .putInt(KEY_MAINT_THRESHOLD_KM, settings.maintenanceAlertThresholdKm)
        .putBoolean(KEY_PRIVACY_MASKING, settings.defaultPrivacyMasking)
        .putBoolean(KEY_DETAIL_AXIS_LABELS, settings.showDetailAxisLabels)
        .apply()
}

class MainActivity : ComponentActivity() {

    private val viewModel: CarValueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MyApplicationTheme(darkTheme = uiState.appSettings.isDarkMode) {
                CarValueCoachApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CarValueCoachApp(viewModel: CarValueViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val vehicleProfile by viewModel.vehicleProfile.collectAsStateWithLifecycle()
    val drivingSessions by viewModel.drivingSessions.collectAsStateWithLifecycle()
    val coachingGoals by viewModel.coachingGoals.collectAsStateWithLifecycle()
    val maintenanceSchedules by viewModel.maintenanceSchedules.collectAsStateWithLifecycle()
    val maintenanceRecords by viewModel.maintenanceRecords.collectAsStateWithLifecycle()
    val currentScenario by viewModel.currentScenario.collectAsStateWithLifecycle()
    val garageVehicles by viewModel.garageVehicles.collectAsStateWithLifecycle()
    val registeredDrivers by viewModel.registeredDrivers.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initSettings(loadAppSettings(context))
    }

    val updateAndPersistSettings: (AppSettings) -> Unit = { newSettings ->
        viewModel.updateSettings(newSettings)
        saveAppSettings(context, newSettings)
    }

    if (!uiState.isLoggedIn) {
        LoginScreen(
            isAuthenticating = uiState.isAuthenticating,
            loginError = uiState.loginError,
            isDarkMode = uiState.appSettings.isDarkMode,
            onToggleDarkMode = { dark ->
                updateAndPersistSettings(uiState.appSettings.copy(isDarkMode = dark))
            },
            onLoginSubmit = { userId, password ->
                viewModel.loginWithHyundaiAccount(userId, password)
            }
        )
        return
    }

    val activeGoal = coachingGoals.firstOrNull { it.isCurrentFocus }
    val recentTrip = drivingSessions.firstOrNull()

    val snackbarHostState = remember { SnackbarHostState() }

    // Feedback message handler for Snackbars
    LaunchedEffect(uiState.feedbackMessage) {
        uiState.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearFeedbackMessage()
        }
    }

    // Back handler to navigate back to Home if in another tab
    if (uiState.currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.setTab(AppTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CleanWhite,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .testTag("app_snackbar_host")
            ) { data ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.widthIn(max = 300.dp),
                        shape = RoundedCornerShape(22.dp),
                        color = BrandNavyContainer,
                        shadowElevation = 4.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A3E5C))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = AlwaysWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = data.visuals.message,
                                color = AlwaysWhite,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = CleanWhite,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        androidx.compose.foundation.BorderStroke(0.8.dp, SurfaceBorderSubtle)
                    )
                    .testTag("bottom_nav_bar")
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = uiState.currentTab == tab
                    val (icon, selectedIcon) = when (tab) {
                        AppTab.HOME -> Icons.Outlined.DirectionsCar to Icons.Filled.DirectionsCar
                        AppTab.TRIPS_COACHING -> Icons.Outlined.Insights to Icons.Filled.Insights
                        AppTab.MAINTENANCE -> Icons.Outlined.Build to Icons.Filled.Build
                        AppTab.REPORT -> Icons.Outlined.Assessment to Icons.Filled.Assessment
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) selectedIcon else icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryNavy,
                            selectedTextColor = PrimaryNavy,
                            indicatorColor = ChipSensorBg,
                            unselectedIconColor = TextTertiary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CleanWhite)
        ) {
            when (uiState.currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        vehicle = vehicleProfile,
                        activeGoal = activeGoal,
                        recentTrip = recentTrip,
                        garageVehicles = garageVehicles,
                        loggedInUserName = uiState.loggedInUserName,
                        onSelectVehicle = { id -> viewModel.selectGarageVehicle(id) },
                        onAddVehicleClick = { viewModel.setShowAddVehicleDialog(true) },
                        onStartDriveClick = { viewModel.setShowLiveDriveModal(true) },
                        onRegisterMaintenanceClick = { viewModel.setShowRegisterMaintenance(true) },
                        onNavigateTab = { tab -> viewModel.setTab(tab) },
                        onTripClick = { trip -> viewModel.openTripDetail(trip) },
                        onTelemetryClick = { viewModel.setShowTelemetryModal(true) },
                        onDemoScenarioClick = { viewModel.setShowDemoScenarioModal(true) },
                        onSettingsClick = { viewModel.setShowSettingsDialog(true) },
                        onOpenDetailedGuideClick = { viewModel.setShowAppGuideDialog(true) },
                        onVehicleCardClick = { viewModel.setShowVehicleDetailModal(true) }
                    )
                }
                AppTab.TRIPS_COACHING -> {
                    TripsCoachingScreen(
                        sessions = drivingSessions,
                        coachingGoals = coachingGoals,
                        activeFilter = uiState.tripFilter,
                        onFilterChange = { filter -> viewModel.setTripFilter(filter) },
                        onGoalSelect = { goalId -> viewModel.selectCoachingGoal(goalId) },
                        onTripClick = { trip -> viewModel.openTripDetail(trip) }
                    )
                }
                AppTab.MAINTENANCE -> {
                    MaintenanceScreen(
                        vehicle = vehicleProfile,
                        schedules = maintenanceSchedules,
                        records = maintenanceRecords,
                        onRegisterMaintenanceClick = { viewModel.setShowRegisterMaintenance(true) },
                        onRecordClick = { record -> viewModel.openMaintenanceDetail(record) },
                        onSelectCondition = { cond -> viewModel.updateUsageCondition(cond) },
                        onOpenManualClick = { viewModel.setShowOfficialManualDialog(true) }
                    )
                }
                AppTab.REPORT -> {
                    ValueReportScreen(
                        report = viewModel.getTransparencyReport(),
                        onShareReportClick = { viewModel.setShowReportShareModal(true) },
                        onToggleMaskPlate = { viewModel.toggleMaskPlate() },
                        onToggleMaskRoute = { viewModel.toggleMaskRoute() },
                        onOpenGradeGuidelineClick = { viewModel.setShowGradeGuidelineDialog(true) }
                    )
                }
            }
        }
    }

    // Modal: Live Drive Session Recorder & Minimal Distraction Driver View
    if (uiState.showLiveDriveModal) {
        DriveRecordingModal(
            isSimulating = uiState.isSimulatingDrive,
            currentSpeedKmh = uiState.simCurrentSpeedKmh,
            distanceKm = uiState.simDistanceKm,
            efficiencyKml = uiState.simEfficiencyKml,
            durationSec = uiState.simDurationSec,
            finishedSession = uiState.simDriveFinishedSession,
            onStartDrive = { viewModel.startDriveSimulation() },
            onStopDrive = { viewModel.stopDriveSimulation() },
            onDismiss = { viewModel.dismissDriveModal() }
        )
    }

    // Modal: Add New Vehicle to Garage (다차량 보유 관리)
    if (uiState.showAddVehicleDialog) {
        AddVehicleDialog(
            onConfirm = { nickname, model, plate, powertrain, driver ->
                viewModel.addGarageVehicle(nickname, model, plate, powertrain, driver)
            },
            onDismiss = { viewModel.setShowAddVehicleDialog(false) }
        )
    }

    // Modal: Maintenance Register with OCR extraction verification
    if (uiState.showRegisterMaintenanceModal) {
        MaintenanceRegisterDialog(
            currentMileageKm = vehicleProfile.currentMileageKm,
            onConfirm = { workshop, mileage, items, actionType, cost, isVerified, verificationLevel, docTitle, memo ->
                viewModel.registerMaintenance(
                    workshopName = workshop,
                    mileageKm = mileage,
                    items = items,
                    actionType = actionType,
                    totalCostWon = cost,
                    isDocumentVerified = isVerified,
                    verificationLevel = verificationLevel,
                    documentTitle = docTitle,
                    receiptMemo = memo
                )
            },
            onDismiss = { viewModel.setShowRegisterMaintenance(false) }
        )
    }

    // Modal: Detailed Trip Inspector
    if (uiState.selectedTripDetail != null) {
        TripDetailDialog(
            session = uiState.selectedTripDetail!!,
            onSaveMemo = { tripId, memo -> viewModel.saveTripSituationMemo(tripId, memo) },
            onDismiss = { viewModel.closeTripDetail() }
        )
    }

    // Modal: Vehicle Specs, Driver Selector & AAOS Capabilities Modal
    if (uiState.showVehicleDetailModal) {
        VehicleDetailModal(
            vehicle = vehicleProfile,
            registeredDrivers = registeredDrivers,
            onSelectDriver = { driver -> viewModel.updateCurrentDriver(driver) },
            onAddDriver = { driver -> viewModel.addCustomDriver(driver) },
            onRemoveDriver = { driver -> viewModel.removeCustomDriver(driver) },
            onSelectCondition = { cond -> viewModel.updateUsageCondition(cond) },
            onDismiss = { viewModel.setShowVehicleDetailModal(false) }
        )
    }

    // Modal: Verified Maintenance Record & Invoice Hash Viewer
    if (uiState.selectedMaintenanceDetail != null) {
        MaintenanceDetailDialog(
            record = uiState.selectedMaintenanceDetail!!,
            onDismiss = { viewModel.closeMaintenanceDetail() }
        )
    }

    // Modal: Vehicle CAN Bus Real-time Telemetry & Diagnostic
    if (uiState.showTelemetryModal) {
        VehicleTelemetryDialog(
            vehicle = vehicleProfile,
            onDismiss = { viewModel.setShowTelemetryModal(false) }
        )
    }

    // Modal: Demo Scenario Switcher for Testers & Evaluators
    if (uiState.showDemoScenarioModal) {
        DemoScenarioDialog(
            currentScenario = currentScenario,
            onSelectScenario = { scenario -> viewModel.applyDemoScenario(scenario) },
            onDismiss = { viewModel.setShowDemoScenarioModal(false) }
        )
    }

    // Modal: Report Sharing and Export
    if (uiState.showReportShareModal) {
        ReportShareDialog(
            report = viewModel.getTransparencyReport(),
            onDismiss = {
                viewModel.setShowReportShareModal(false)
                viewModel.setFeedbackMessage("가치 리포트 공유 링크가 클립보드에 복사되었습니다.")
            }
        )
    }

    // Modal: Hyundai Official Manual Viewer
    if (uiState.showOfficialManualDialog) {
        OfficialManualDialog(
            vehicle = vehicleProfile,
            onDismiss = { viewModel.setShowOfficialManualDialog(false) }
        )
    }

    // Modal: App Settings & Dark Mode Preferences
    if (uiState.showSettingsDialog) {
        SettingsDialog(
            settings = uiState.appSettings,
            loggedInUserName = uiState.loggedInUserName,
            loggedInUserId = uiState.loggedInUserId,
            onUpdateSettings = updateAndPersistSettings,
            onLogoutClick = { viewModel.logout() },
            onDismiss = { viewModel.setShowSettingsDialog(false) }
        )
    }

    // Modal: Transparency Grade Criteria & Guidelines
    if (uiState.showGradeGuidelineDialog) {
        GradeGuidelineDialog(
            report = viewModel.getTransparencyReport(),
            onDismiss = { viewModel.setShowGradeGuidelineDialog(false) }
        )
    }

    // Modal: Detailed App Introduction & Usage Guide
    if (uiState.showAppGuideDialog) {
        AppGuideDialog(
            onDismiss = { viewModel.setShowAppGuideDialog(false) }
        )
    }
}

