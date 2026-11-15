package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.DrivingCoaching
import com.example.DriveJournal
import com.example.DrivingRecordService
import com.example.data.model.CoachingGoal
import com.example.data.model.DrivingSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TripsCoachingScreen(
    sessions: List<DrivingSession>,
    coachingGoals: List<CoachingGoal>,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    onGoalSelect: (String) -> Unit,
    onTripClick: (DrivingSession) -> Unit,
    modifier: Modifier = Modifier
) {
    var demo by rememberSaveable { mutableStateOf(false) }
    Column(modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(selected = !demo, onClick = { demo = false }, label = { Text("Pleos 수신 기록") })
            FilterChip(selected = demo, onClick = { demo = true }, label = { Text("예시 코칭") })
        }
        if (demo) {
            Text("아래 연비·AI 과제·주행 세션은 시연용 예시입니다. 수신 기록의 분석 결과가 아닙니다.",
                Modifier.padding(horizontal = 20.dp, vertical = 8.dp), style = MaterialTheme.typography.bodyMedium)
            DemoTripsCoachingScreen(sessions, coachingGoals, activeFilter, onFilterChange, onGoalSelect, onTripClick, Modifier.weight(1f))
        } else {
            PleosRecordsScreen(Modifier.weight(1f))
        }
    }
}

@Composable
private fun PleosRecordsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val status by DrivingRecordService.recordStatus.collectAsStateWithLifecycle()
    var refresh by remember { mutableIntStateOf(0) }
    var records by remember { mutableStateOf<List<DriveJournal.Companion.Summary>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) refresh++
        }
        owner.lifecycle.addObserver(observer)
        onDispose { owner.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(refresh, status) {
        loading = true
        error = null
        val result = withContext(Dispatchers.IO) {
            runCatching {
                val directory = File(context.filesDir, "driving_records")
                val files = if (!directory.exists()) emptyList() else
                    (directory.listFiles() ?: error("기록 폴더를 읽을 수 없습니다")).toList()
                files.filter { it.extension == "csv" }.sortedByDescending { it.name }.take(20)
                    .map { DriveJournal.read(it, DrivingRecordService.activeFileName) }
            }
        }
        records = result.getOrDefault(emptyList())
        error = result.exceptionOrNull()?.let { "기록을 불러오지 못했습니다. 새로고침으로 다시 시도해 주세요." }
        loading = false
    }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("주행 기록 & 분석", style = MaterialTheme.typography.headlineSmall)
            Text("1호차 · Pleos 에뮬레이터에서 수신한 기록 · 최근 20건", style = MaterialTheme.typography.bodyMedium)
            Text("현재 선택한 차와 관계없이 1호차 테스트 기록을 표시합니다.", style = MaterialTheme.typography.bodySmall)
            Text(status, Modifier.padding(top = 8.dp))
            TextButton(onClick = { refresh++ }, enabled = !loading) { Text("기록 새로고침") }
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("시연용 분석 v1", style = MaterialTheme.typography.titleMedium)
                    Text("급가속·급감속 후보는 속도 변화가 ±3m/s² 이상으로 0.8초 이상 이어진 구간입니다. 0.1~3초 간격의 전진 속도만 분석하며 수신 공백과 후진은 제외합니다.")
                    Text("후보 0회는 안전 운전을 보증하지 않습니다. 연비·차량 상태 등급은 이 기록으로 계산하지 않습니다.")
                }
            }
        }
        when {
            loading -> item { Text("기록을 불러오는 중…") }
            error != null -> item { Text(error!!, color = MaterialTheme.colorScheme.error) }
            records.isEmpty() -> item { Text("아직 저장된 기록이 없습니다. 정차 상태에서 상단 ‘기록 시작’을 누르고 테스트한 뒤 ‘기록 종료’를 누르면 여기에 표시됩니다.") }
            else -> items(records, key = { it.name }) { record ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(SimpleDateFormat("MM/dd HH:mm:ss", Locale.KOREA).format(Date(record.startedMs)), style = MaterialTheme.typography.titleLarge)
                        Text(record.state, style = MaterialTheme.typography.titleMedium)
                        Text("관측 구간 ${record.durationMs / 1000}초 · 속도 ${record.count}개 · 누락/오류 알림 ${record.gaps}개")
                        Text("최고 속도: ${record.maxKmh?.let { String.format(Locale.KOREA, "%.1f km/h", it) } ?: "데이터 없음"}")
                        Text("분석 가능 구간: ${String.format(Locale.KOREA, "%.1f", record.analyzedSeconds)}초")
                        if (record.analyzedSeconds > 0) {
                            Text("급가속 후보 ${record.accelerationEvents}회 · 급감속 후보 ${record.brakingEvents}회", style = MaterialTheme.typography.titleMedium)
                        } else {
                            Text("분석 가능한 연속 속도 데이터가 부족합니다.")
                        }
                        val coaching = DrivingCoaching.from(record)
                        HorizontalDivider()
                        Text("규칙 기반 코칭 · 시연용", style = MaterialTheme.typography.labelLarge)
                        Text(coaching.title, style = MaterialTheme.typography.titleMedium)
                        Text(coaching.message)
                        coaching.limitations.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
    }
}
