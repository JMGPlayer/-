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
import android.util.Log
import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.api.model.area.DoorArea
import ai.pleos.playground.vehicle.constant.area.DoorValue
import ai.pleos.playground.vehicle.listener.DistanceDrivenListener
import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager

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

    private val recordNotificationPermissionLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                startDrivingRecordService()
            } else {
                android.widget.Toast.makeText(
                    this,
                    "이번 테스트는 알림 권한을 허용한 뒤 시작해 주세요.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun requestDrivingRecordStart() {
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                "android.permission.POST_NOTIFICATIONS"
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            recordNotificationPermissionLauncher.launch(
                "android.permission.POST_NOTIFICATIONS"
            )
        } else {
            startDrivingRecordService()
        }
    }

    private fun startDrivingRecordService() {
        androidx.core.content.ContextCompat.startForegroundService(
            this,
            android.content.Intent(
                this,
                DrivingRecordService::class.java
            )
        )
    }

    private val speedStatus =
        androidx.compose.runtime.mutableStateOf("현재 속도: 연결 전")

    private var speedCar: Car? = null
    private var speedManager: CarPropertyManager? = null
    private var speedCallback:
            CarPropertyManager.CarPropertyEventCallback? = null

    private var speedSession = 0

    private fun startSpeedUpdates() {
        if (speedCar != null) return

        val permission = "android.car.permission.CAR_SPEED"
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                permission
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            speedStatus.value = "현재 속도: 접근 권한이 필요합니다."
            return
        }

        if (!packageManager.hasSystemFeature(
                android.content.pm.PackageManager.FEATURE_AUTOMOTIVE
            )
        ) {
            speedStatus.value = "현재 속도: 차량용 환경에서 확인해 주세요."
            return
        }

        val session = ++speedSession
        speedStatus.value = "현재 속도: 차량 서비스 연결 중…"

        try {
            speedCar = Car.createCar(
                this,
                android.os.Handler(android.os.Looper.getMainLooper()),
                Car.CAR_WAIT_TIMEOUT_DO_NOT_WAIT
            ) { car, ready ->
                if (session != speedSession || isDestroyed || isFinishing) {
                    return@createCar
                }

                if (!ready) {
                    speedManager = null
                    speedCallback = null
                    speedStatus.value = "현재 속도: 차량 서비스 연결 끊김"
                    return@createCar
                }

                try {
                    val manager =
                        car.getCarManager(Car.PROPERTY_SERVICE)
                                as CarPropertyManager

                    val callback = object :
                        CarPropertyManager.CarPropertyEventCallback {

                        override fun onChangeEvent(
                            value: CarPropertyValue<*>
                        ) {
                            if (session != speedSession ||
                                isDestroyed || isFinishing
                            ) return

                            if (value.propertyId !=
                                VehiclePropertyIds.PERF_VEHICLE_SPEED
                            ) return

                            val metersPerSecond = value.value as? Float

                            if (value.status !=
                                CarPropertyValue.STATUS_AVAILABLE ||
                                metersPerSecond == null ||
                                !metersPerSecond.isFinite()
                            ) {
                                speedStatus.value =
                                    "현재 속도: 유효한 데이터가 없습니다."
                                return
                            }

                            val kmh = kotlin.math.abs(metersPerSecond) * 3.6f
                            val direction =
                                if (metersPerSecond < 0f) " (후진)" else ""

                            speedStatus.value = String.format(
                                java.util.Locale.KOREA,
                                "현재 속도: %.1f km/h%s",
                                kmh,
                                direction
                            )
                        }

                        override fun onErrorEvent(
                            propertyId: Int,
                            areaId: Int
                        ) {
                            if (session != speedSession) return

                            if (propertyId ==
                                VehiclePropertyIds.PERF_VEHICLE_SPEED
                            ) {
                                speedStatus.value = "현재 속도: 수신 오류"
                                Log.w("PleosTest", "속도 수신 오류")
                            }
                        }
                    }

                    speedManager = manager
                    speedCallback = callback
                    speedStatus.value = "현재 속도: 데이터 수신 대기 중…"

                    val registered = manager.registerCallback(
                        callback,
                        VehiclePropertyIds.PERF_VEHICLE_SPEED,
                        2f
                    )

                    if (!registered) {
                        speedStatus.value =
                            "현재 속도: 이 환경에서 속도 수신을 등록하지 못했습니다."
                    }

                    Log.i("PleosTest", "속도 수신 등록 결과: $registered")
                } catch (e: Exception) {
                    speedStatus.value = "속도 연결 실패: ${e.message}"
                    Log.e("PleosTest", "속도 연결 실패", e)
                }
            }
        } catch (e: Exception) {
            speedStatus.value = "속도 서비스 연결 실패: ${e.message}"
            Log.e("PleosTest", "속도 서비스 연결 실패", e)
            stopSpeedUpdates()
        }
    }

    private fun stopSpeedUpdates() {
        speedSession++

        try {
            speedCallback?.let { callback ->
                speedManager?.unregisterCallback(callback)
            }
        } catch (e: Exception) {
            Log.w("PleosTest", "속도 수신 해제 실패", e)
        }

        try {
            speedCar?.disconnect()
        } catch (e: Exception) {
            Log.w("PleosTest", "속도 서비스 연결 해제 실패", e)
        }

        speedCallback = null
        speedManager = null
        speedCar = null
        speedStatus.value = "현재 속도: 수신 중지"
    }

    private val speedPermissionStatus =
        androidx.compose.runtime.mutableStateOf(
            "속도 정보를 읽으려면 권한을 허용해 주세요."
        )

    private val speedPermissionLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted ->
        speedPermissionStatus.value = if (granted) {
            "속도 접근 권한이 허용되었습니다."
        } else {
            "속도 접근 권한이 허용되지 않았습니다."
        }

        Log.i("PleosTest", speedPermissionStatus.value)
    }

    private fun requestSpeedPermission() {
        val permission = "android.car.permission.CAR_SPEED"

        if (
            androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            speedPermissionStatus.value =
                "속도 접근 권한이 이미 허용되어 있습니다."
        } else {
            speedPermissionLauncher.launch(permission)
        }
    }

    private val viewModel: CarValueViewModel by viewModels()
    private var pleosVehicle: Vehicle? = null
    private var mileageUpdatesActive = false

    private val mileageListener = object : DistanceDrivenListener {
        override fun onDistanceDrivenUpdated(value: Float?) {
            runOnUiThread {
                if (!mileageUpdatesActive || isDestroyed || isFinishing) {
                    return@runOnUiThread
                }

                if (value == null || !value.isFinite() || value < 0f ||
                    value.toDouble() > Int.MAX_VALUE.toDouble()
                ) {
                    mileageStatus.value =
                        "자동 갱신: 유효한 값이 없습니다. 차량 카드는 마지막 값을 유지합니다."
                    return@runOnUiThread
                }

                savePleosMileage(value)

                val formattedKm = String.format(
                    java.util.Locale.KOREA,
                    "%,.1f",
                    value
                )
                val updatedAt = java.text.SimpleDateFormat(
                    "HH:mm:ss",
                    java.util.Locale.KOREA
                ).format(java.util.Date())

                mileageStatus.value =
                    "누적 주행거리: $formattedKm km\n자동 수신: $updatedAt"

                Log.i("PleosTest", "주행거리 자동 수신: $value km")
            }
        }

        override fun onFailed(e: Exception) {
            Log.e("PleosTest", "주행거리 자동 수신 실패", e)

            runOnUiThread {
                if (mileageUpdatesActive && !isDestroyed && !isFinishing) {
                    mileageStatus.value =
                        "자동 수신 실패: ${e.message ?: e.javaClass.simpleName}\n" +
                                "차량 카드는 마지막 값을 유지합니다."
                }
            }
        }
    }
    private val mileageStatus = androidx.compose.runtime.mutableStateOf(
        "누적 주행거리: 아직 조회하지 않았습니다."
    )
    private val isMileageReading = androidx.compose.runtime.mutableStateOf(false)

    private val doorStatus = androidx.compose.runtime.mutableStateOf(
        "아직 조회하지 않았습니다."
    )
    private val isReading = androidx.compose.runtime.mutableStateOf(false)
    private val showPleosDiagnostics = androidx.compose.runtime.mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        restorePleosMileage()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = uiState.appSettings.isDarkMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    CarValueCoachApp(
                        viewModel = viewModel,
                        onOpenPleosDiagnostics = { showPleosDiagnostics.value = true }
                    )

                    if (showPleosDiagnostics.value) {
                        PleosDiagnosticsDialog(
                            mileageStatus = mileageStatus.value,
                            isMileageReading = isMileageReading.value,
                            onReadMileage = ::readPleosMileage,
                            speedPermissionStatus = speedPermissionStatus.value,
                            onRequestSpeedPermission = ::requestSpeedPermission,
                            speedStatus = speedStatus.value,
                            onStartRecording = ::requestDrivingRecordStart,
                            onStopRecording = {
                                startService(
                                    android.content.Intent(
                                        this@MainActivity,
                                        DrivingRecordService::class.java
                                    ).setAction("STOP_RECORDING")
                                )
                            },
                            doorStatus = doorStatus.value,
                            isDoorReading = isReading.value,
                            onReadDoor = ::readPleosDoor,
                            onDismiss = { showPleosDiagnostics.value = false }
                        )
                    }
                }
            }
        }

        readPleosDoor()
    }

    private fun readPleosDoor() {
        if (isReading.value) return

        isReading.value = true
        doorStatus.value = "에뮬레이터의 문 위치를 조회하고 있습니다."

        try {
            val vehicle = pleosVehicle ?: Vehicle(this).also {
                pleosVehicle = it
                it.initialize()
            }

            Log.i("PleosTest", "문 상태 조회 요청")

            vehicle.getDoor().getCurrentDoorPosition(
                area = DoorArea(DoorValue.ROW_1_LEFT),
                onComplete = { position ->
                    val message = if (position == null) {
                        "응답은 왔지만 문 위치 값이 없습니다."
                    } else {
                        "앞좌석 왼쪽 문 위치 값: $position"
                    }

                    Log.i("PleosTest", message)
                    showDoorResult(message)
                },
                onFailed = { error ->
                    Log.e("PleosTest", "문 상태 조회 실패", error)
                    showDoorResult(
                        "조회 실패: ${error.message ?: error.javaClass.simpleName}"
                    )
                }
            )
        } catch (error: Exception) {
            Log.e("PleosTest", "SDK 연결 또는 조회 요청 실패", error)
            showDoorResult(
                "연결 또는 조회 실패: ${error.message ?: error.javaClass.simpleName}"
            )
        }
    }

    private fun showDoorResult(message: String) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                doorStatus.value = message
                isReading.value = false
            }
        }
    }

    private fun readPleosMileage() {
        if (isMileageReading.value) return

        isMileageReading.value = true
        mileageStatus.value = "누적 주행거리 조회 중…"

        try {
            val vehicle = pleosVehicle ?: Vehicle(this).also {
                pleosVehicle = it
                it.initialize()
            }

            Log.i("PleosTest", "누적 주행거리 조회 요청")

            vehicle.getOdometer().getCurrentDistanceDriven(
                onComplete = { km ->
                    if (km != null && km.isFinite() && km >= 0f &&
                        km.toDouble() <= Int.MAX_VALUE.toDouble()
                    ) {
                        runOnUiThread {
                            if (!isDestroyed && !isFinishing) {
                                savePleosMileage(km)
                            }
                        }
                    }
                    val message = when {
                        km == null ->
                            "누적 주행거리: 제공된 값이 없습니다."

                        !km.isFinite() || km < 0f ->
                            "누적 주행거리: 유효하지 않은 값입니다."

                        else ->
                            "누적 주행거리: ${
                                String.format(java.util.Locale.KOREA, "%,.1f", km)
                            } km"
                    }

                    Log.i("PleosTest", message)
                    showMileageResult(message)
                },
                onFailed = { error ->
                    Log.e("PleosTest", "누적 주행거리 조회 실패", error)
                    showMileageResult(
                        "주행거리 조회 실패: ${
                            error.message ?: error.javaClass.simpleName
                        }"
                    )
                }
            )
        } catch (error: Exception) {
            Log.e("PleosTest", "주행거리 연결 또는 요청 실패", error)
            showMileageResult(
                "주행거리 연결 또는 요청 실패: ${
                    error.message ?: error.javaClass.simpleName
                }"
            )
        }
    }

    private fun showMileageResult(message: String) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                mileageStatus.value = message
                isMileageReading.value = false
            }
        }
    }

    private fun savePleosMileage(km: Float) {
        val queriedAt = System.currentTimeMillis()

        getSharedPreferences("pleos_vehicle_cache", Context.MODE_PRIVATE)
            .edit()
            .putFloat("veh_sonata_mileage_km", km)
            .putLong("veh_sonata_queried_at", queriedAt)
            .apply()

        viewModel.updatePleosMileage(km, queriedAt)
    }

    private fun restorePleosMileage() {
        val prefs = getSharedPreferences(
            "pleos_vehicle_cache",
            Context.MODE_PRIVATE
        )

        if (!prefs.contains("veh_sonata_mileage_km")) return

        val km = prefs.getFloat("veh_sonata_mileage_km", -1f)
        val queriedAt = prefs.getLong("veh_sonata_queried_at", 0L)

        if (!km.isFinite() || km < 0f ||
            km.toDouble() > Int.MAX_VALUE.toDouble() ||
            queriedAt <= 0L
        ) return

        viewModel.updatePleosMileage(km, queriedAt)

        val formattedKm = String.format(
            java.util.Locale.KOREA,
            "%,.1f",
            km
        )
        val formattedTime = java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            java.util.Locale.KOREA
        ).format(java.util.Date(queriedAt))

        mileageStatus.value =
            "저장된 주행거리: $formattedKm km\n마지막 조회: $formattedTime"
    }

    override fun onStart() {
        super.onStart()
        startSpeedUpdates()

        if (mileageUpdatesActive) return

        try {
            val vehicle = pleosVehicle ?: Vehicle(this).also {
                pleosVehicle = it
                it.initialize()
            }

            mileageUpdatesActive = true
            vehicle.getOdometer().registerDistanceDriven(mileageListener)

            Log.i("PleosTest", "주행거리 자동 수신 등록 요청")

            // 변경 알림과 별도로, 화면을 열었을 때 현재 값도 조회합니다.
            readPleosMileage()
        } catch (e: Exception) {
            mileageUpdatesActive = false
            Log.e("PleosTest", "주행거리 자동 수신 등록 실패", e)
            showMileageResult(
                "자동 수신 등록 실패: ${e.message ?: e.javaClass.simpleName}"
            )
        }
    }

    override fun onStop() {
        stopSpeedUpdates()
        val wasActive = mileageUpdatesActive
        mileageUpdatesActive = false

        try {
            if (wasActive) {
                pleosVehicle?.getOdometer()
                    ?.unregisterDistanceDriven(mileageListener)
            }
        } catch (e: Exception) {
            Log.e("PleosTest", "주행거리 자동 수신 해제 실패", e)
        } finally {
            super.onStop()
        }
    }

    override fun onDestroy() {
        try {
            pleosVehicle?.release()
        } finally {
            pleosVehicle = null
            super.onDestroy()
        }
    }
}

@Composable
fun CarValueCoachApp(
    viewModel: CarValueViewModel,
    onOpenPleosDiagnostics: () -> Unit = {}
) {
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
            onConnectionCheckClick = {
                viewModel.setShowSettingsDialog(false)
                onOpenPleosDiagnostics()
            },
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

