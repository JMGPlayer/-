package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun PleosDiagnosticsDialog(
    mileageStatus: String,
    isMileageReading: Boolean,
    onReadMileage: () -> Unit,
    speedPermissionStatus: String,
    onRequestSpeedPermission: () -> Unit,
    speedStatus: String,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    doorStatus: String,
    isDoorReading: Boolean,
    onReadDoor: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .widthIn(max = 560.dp)
                .fillMaxHeight(0.88f)
                .testTag("pleos_diagnostics_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "차량 연결 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pleos 에뮬레이터·차량에서 수신 상태를 확인하는 시연 도구입니다. 일반 스마트폰에서는 차량 데이터가 제공되지 않습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("누적 주행거리", style = MaterialTheme.typography.titleSmall)
                    Text(mileageStatus, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        onClick = onReadMileage,
                        enabled = !isMileageReading,
                        modifier = Modifier.heightIn(min = 40.dp)
                    ) {
                        Text(if (isMileageReading) "조회 중…" else "주행거리 다시 읽기")
                    }

                    HorizontalDivider()
                    Text("속도", style = MaterialTheme.typography.titleSmall)
                    Text(speedPermissionStatus, style = MaterialTheme.typography.bodySmall)
                    Text(speedStatus, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        onClick = onRequestSpeedPermission,
                        modifier = Modifier.heightIn(min = 40.dp)
                    ) { Text("속도 접근 허용") }

                    HorizontalDivider()
                    Text("주행 기록", style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = onStartRecording) { Text("기록 시작") }
                        OutlinedButton(onClick = onStopRecording) { Text("기록 종료") }
                    }
                    DrivingRecordPanel()

                    HorizontalDivider()
                    Text("문 상태", style = MaterialTheme.typography.titleSmall)
                    Text(doorStatus, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        onClick = onReadDoor,
                        enabled = !isDoorReading,
                        modifier = Modifier.heightIn(min = 40.dp)
                    ) {
                        Text(if (isDoorReading) "조회 중…" else "문 상태 다시 읽기")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("닫기") }
                }
            }
        }
    }
}
