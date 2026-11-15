package com.example

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DrivingRecordPanel() {
    val context = LocalContext.current
    val status by DrivingRecordService.recordStatus.collectAsStateWithLifecycle()
    var open by remember { mutableStateOf(false) }
    var refresh by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf("불러오는 중…") }
    Text(status)
    TextButton(onClick = { open = true }) { Text("저장된 주행 기록 보기") }
    if (open) {
        LaunchedEffect(refresh) {
            result = "불러오는 중…"
            result = withContext(Dispatchers.IO) {
                runCatching {
                    val files = File(context.filesDir, "driving_records").listFiles()
                        ?.filter { it.extension == "csv" }
                        ?.sortedByDescending { it.name }?.take(20).orEmpty()
                    if (files.isEmpty()) "저장된 주행 기록이 없습니다."
                    else files.joinToString("\n\n") { file ->
                        val r = DriveJournal.read(file, DrivingRecordService.activeFileName)
                        val date = SimpleDateFormat("MM/dd HH:mm:ss", Locale.KOREA).format(Date(r.startedMs))
                        val max = r.maxKmh?.let { String.format(Locale.KOREA, "%.1f km/h", it) } ?: "데이터 없음"
                        "$date · ${r.state}\n관측 구간 ${r.durationMs / 1000}초 · 속도 ${r.count}개\n최고 속도 $max · 누락/오류 알림 ${r.gaps}개\n분석 가능 구간 ${r.analyzedSeconds.toInt()}초\n급가속 후보 ${r.accelerationEvents}회 · 급감속 후보 ${r.brakingEvents}회"
                    }
                }.getOrElse { "기록 읽기 실패: ${it.message}" }
            }
        }
        AlertDialog(
            onDismissRequest = { open = false },
            title = { Text("Pleos 수신 기록 · 최근 20건") },
            text = {
                Column(Modifier.heightIn(max = 320.dp).verticalScroll(rememberScrollState())) {
                    Text("시연용 분석 v1 · 1호차 에뮬레이터 데이터\n속도 변화가 ±3m/s² 이상으로 0.8초 이상 이어지면 한 구간을 1회로 셉니다. 0.1~3초 간격의 전진 속도만 분석하며 수신 공백·후진은 제외합니다.\n0회는 안전 운전을 보증하지 않습니다. 차량 상태 등급·공인 평가가 아닙니다.\n\n$result")
                }
            },
            confirmButton = { TextButton(onClick = { open = false }) { Text("닫기") } },
            dismissButton = { TextButton(onClick = { refresh++ }) { Text("새로고침") } }
        )
    }
}
