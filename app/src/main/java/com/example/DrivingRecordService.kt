package com.example

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.Locale
import kotlin.math.abs

class DrivingRecordService : Service() {

    private var vehicleCar: Car? = null
    private var propertyManager: CarPropertyManager? = null
    private var speedCallback:
            CarPropertyManager.CarPropertyEventCallback? = null

    companion object {
        val recordStatus = kotlinx.coroutines.flow.MutableStateFlow("주행 기록 대기")
        @Volatile var activeFileName: String? = null
            private set
    }
    private val disk = java.util.concurrent.Executors.newSingleThreadExecutor()
    private var journal: DriveJournal? = null
    private var diskFailed = false
    private var endReason = "service_stopped"

    private fun persist(block: () -> Unit) {
        disk.execute {
            if (!diskFailed) try { block() } catch (e: Exception) {
                diskFailed = true
                runCatching { journal?.abort() }
                activeFileName = null
                recordStatus.value = "주행 기록 저장 실패: ${e.message}"
                Log.e("PleosRecord", "주행 기록 저장 실패", e)
                Handler(Looper.getMainLooper()).post { stopSelf() }
            }
        }
    }

    private fun recordGap(reason: String) {
        val wall = System.currentTimeMillis()
        val elapsed = SystemClock.elapsedRealtime()
        persist { journal?.gap(wall, elapsed, reason) }
    }

    private var recording = false
    private var lastLogTime = 0L

    override fun onCreate() {
        super.onCreate()

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(
                "driving_record",
                "주행 기록",
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == "STOP_RECORDING") {
            endReason = "user_stop"
            recording = false
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        val stopIntent = Intent(this, DrivingRecordService::class.java)
            .setAction("STOP_RECORDING")

        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(this, "driving_record")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("Car Value Coach")
            .setContentText("주행 속도를 기기에 저장하고 있습니다.")
            .setOngoing(true)
            .addAction(
                Notification.Action.Builder(
                    android.R.drawable.ic_media_pause,
                    "기록 종료",
                    stopPendingIntent
                ).build()
            )
            .build()

        startForeground(1001, notification)

        // 시작 버튼을 여러 번 눌러도 중복 등록하지 않습니다.
        if (recording) return START_NOT_STICKY

        if (ContextCompat.checkSelfPermission(
                this,
                "android.car.permission.CAR_SPEED"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("PleosRecord", "속도 권한이 없어 서비스를 종료합니다.")
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        if (!packageManager.hasSystemFeature(
                PackageManager.FEATURE_AUTOMOTIVE
            )
        ) {
            Log.e("PleosRecord", "차량용 환경이 아니므로 종료합니다.")
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        val startedWall = System.currentTimeMillis()
        val startedElapsed = SystemClock.elapsedRealtime()
        recordStatus.value = "주행 기록 시작 중…"
        persist {
            journal = DriveJournal(java.io.File(filesDir, "driving_records"), startedWall, startedElapsed)
            activeFileName = journal?.file?.name
            recordStatus.value = "주행 기록 중 · 데이터 수신 대기"
            Log.i("PleosRecord", "주행 파일 생성: $activeFileName")
        }
        recording = true
        lastLogTime = 0L
        Log.i("PleosRecord", "주행 기록 서비스 시작")
        connectSpeed()
        return START_NOT_STICKY
    }

    private fun connectSpeed() {
        try {
            vehicleCar = Car.createCar(
                this,
                Handler(Looper.getMainLooper()),
                Car.CAR_WAIT_TIMEOUT_DO_NOT_WAIT
            ) { car, ready ->
                if (!recording) return@createCar

                if (!ready) {
                    recordGap("disconnected")
                    propertyManager = null
                    speedCallback = null
                    Log.w(
                        "PleosRecord",
                        "차량 서비스 연결 끊김 · 이 구간은 데이터 누락"
                    )
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
                            if (!recording) return
                            if (value.propertyId !=
                                VehiclePropertyIds.PERF_VEHICLE_SPEED
                            ) return

                            if (value.status !=
                                CarPropertyValue.STATUS_AVAILABLE
                            ) {
                                recordGap("unavailable")
                                Log.w("PleosRecord", "속도 데이터 사용 불가")
                                return
                            }

                            val metersPerSecond = value.value as? Float
                            if (metersPerSecond == null ||
                                !metersPerSecond.isFinite()
                            ) {
                                recordGap("invalid_speed")
                                Log.w("PleosRecord", "유효하지 않은 속도")
                                return
                            }

                            val wall = System.currentTimeMillis()
                            val elapsed = SystemClock.elapsedRealtime()
                            val signal = value.timestamp
                            persist {
                                journal?.sample(wall, elapsed, signal, metersPerSecond)
                                recordStatus.value = "주행 기록 중 · ${journal?.samples ?: 0}개 저장"
                            }

                            // 로그가 너무 많아지지 않도록 약 1초 간격으로 출력합니다.
                            val now = SystemClock.elapsedRealtime()
                            if (lastLogTime != 0L &&
                                now - lastLogTime < 1000L
                            ) return
                            lastLogTime = now

                            val kmh = abs(metersPerSecond) * 3.6f
                            val direction =
                                if (metersPerSecond < 0f) "후진" else "전진/정지"

                            Log.i(
                                "PleosRecord",
                                String.format(
                                    Locale.KOREA,
                                    "서비스 속도 수신: %.1f km/h · %s · 신호시각(ns): %d",
                                    kmh,
                                    direction,
                                    value.timestamp
                                )
                            )
                        }

                        override fun onErrorEvent(
                            propertyId: Int,
                            areaId: Int
                        ) {
                            if (!recording) return
                            if (propertyId ==
                                VehiclePropertyIds.PERF_VEHICLE_SPEED
                            ) {
                                recordGap("speed_error")
                                Log.w("PleosRecord", "서비스 속도 수신 오류")
                            }
                        }
                    }

                    propertyManager = manager
                    speedCallback = callback

                    val registered = manager.registerCallback(
                        callback,
                        VehiclePropertyIds.PERF_VEHICLE_SPEED,
                        2f
                    )

                    Log.i(
                        "PleosRecord",
                        "서비스 속도 수신 등록 결과: $registered"
                    )

                    if (!registered) {
                        endReason = "registration_failed"
                        stopSelf()
                    }
                } catch (e: Exception) {
                    endReason = "registration_failed"
                    Log.e("PleosRecord", "속도 수신 등록 실패", e)
                    stopSelf()
                }
            }
        } catch (e: Exception) {
            endReason = "connection_failed"
            Log.e("PleosRecord", "차량 서비스 연결 실패", e)
            stopSelf()
        }
    }

    override fun onDestroy() {
        recording = false

        try {
            speedCallback?.let {
                propertyManager?.unregisterCallback(it)
            }
        } catch (e: Exception) {
            Log.w("PleosRecord", "속도 수신 해제 실패", e)
        }

        try {
            vehicleCar?.disconnect()
        } catch (e: Exception) {
            Log.w("PleosRecord", "차량 서비스 연결 해제 실패", e)
        }

        speedCallback = null
        propertyManager = null
        vehicleCar = null

        val endedWall = System.currentTimeMillis()
        val endedElapsed = SystemClock.elapsedRealtime()
        val reason = endReason
        persist {
            journal?.let {
                it.finish(endedWall, endedElapsed, reason)
                recordStatus.value = if (reason == "user_stop") "주행 기록 저장 완료 · ${it.samples}개" else "주행 기록 중단 · ${it.samples}개 보관"
                Log.i("PleosRecord", "주행 파일 저장: ${it.file.name} · ${it.samples}개 · $reason")
            }
            activeFileName = null
        }
        disk.shutdown()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.i("PleosRecord", "주행 기록 서비스 종료")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}