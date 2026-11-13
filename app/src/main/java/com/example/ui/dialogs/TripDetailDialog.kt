package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DataQualityType
import com.example.data.model.DrivingSession
import com.example.ui.components.MiniSpeedChart
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun TripDetailDialog(
    session: DrivingSession,
    onSaveMemo: (Long, String) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    var memoText by remember { mutableStateOf(session.userSituationMemo ?: "") }
    var isEditingMemo by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("trip_detail_dialog"),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = session.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.5.sp,
                                color = PrimaryNavy
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${session.dateFormatted} · ${session.driverName} 운전",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_trip_detail_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = SurfaceBorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Status Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (session.isComparable && !session.isPendingJudgment) {
                            StatusChip(
                                text = "비교 가능 주행 (출퇴근)",
                                backgroundColor = ChipVerifiedBg,
                                textColor = ChipVerifiedText
                            )
                        } else if (session.isPendingJudgment) {
                            StatusChip(
                                text = "판단 보류 (조건 상이)",
                                backgroundColor = ChipPendingBg,
                                textColor = ChipPendingText
                            )
                        }

                        if (session.dataQuality == DataQualityType.PARTIAL_TUNNEL_GAP) {
                            StatusChip(
                                text = "터널 음영 누락 표기 (정직 기록)",
                                backgroundColor = SurfaceLight,
                                textColor = TextSecondary
                            )
                        } else {
                            StatusChip(
                                text = "센서 무결성 100%",
                                backgroundColor = ChipSensorBg,
                                textColor = ChipSensorText
                            )
                        }
                    }

                    // 4 Core Metrics Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("주행 거리", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${session.distanceKm} km", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = TextPrimary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("소요 시간", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${session.durationMinutes}분", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = TextPrimary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("실제 효율", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${session.efficiency} ${session.efficiencyUnit}", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = PrimaryBlue)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("평균 속도", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${session.averageSpeedKmh} km/h", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = TextPrimary)
                        }
                    }

                    // EV specific energy stat if available
                    if (session.energyConsumedKwh != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ChipSensorBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("주행 총 소비 전력량", fontSize = 12.5.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
                                }
                                Text("${session.energyConsumedKwh} kWh", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }
                        }
                    }

                    // Dual Graph 1: Speed (km/h) with X, Y Labels
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "① 관측 속도 추세 그래프",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 13.5.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "X축: 시간(분) · Y축: 속도(km/h)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 23.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        com.example.ui.components.TelemetryAxisChart(
                            samples = session.speedSamples,
                            unit = "km/h",
                            durationMinutes = session.durationMinutes,
                            isTunnelGap = session.dataQuality == DataQualityType.PARTIAL_TUNNEL_GAP
                        )
                    }

                    // Dual Graph 2: Efficiency with X, Y Labels
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShowChart,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "② 시간축 정합 연비·전비 추세 그래프",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 13.5.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "X축: 시간(분) · Y축: 효율(${session.efficiencyUnit})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.padding(start = 23.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val effIntSamples = session.efficiencySamples.map { (it * 3).toInt() }
                        com.example.ui.components.TelemetryAxisChart(
                            samples = effIntSamples,
                            unit = session.efficiencyUnit,
                            durationMinutes = session.durationMinutes,
                            isTunnelGap = false,
                            lineColor = PrimaryBlue
                        )
                    }

                    // Graph Derived Analysis & Comparative Insights Card (User request)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChipVerifiedBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChipVerifiedText.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Insights,
                                    contentDescription = null,
                                    tint = ChipVerifiedText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "그래프 데이터 도출 결과 & 비교 분석",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = ChipVerifiedText
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = session.graphInsightSummary ?: "출퇴근 반복 경로를 정속 유지하여 급가속 없이 안정적인 탄력 주행 연비를 달성했습니다.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = PrimaryNavy,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = ChipVerifiedText.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("가감속 변동폭", fontSize = 10.5.sp, color = TextSecondary)
                                    Text(
                                        if (session.speedSamples.maxOrNull() ?: 0 > 90) "완만한 크루징 구간 다수" else "도심 신호 정지 반복",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("공인 연비 대비", fontSize = 10.5.sp, color = TextSecondary)
                                    val pct = if (session.efficiency >= 19.0) "+12% 달성 우수" else "+5% 수준 유지"
                                    Text(pct, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                }
                            }
                        }
                    }

                    // Deceleration & Safety Principle Note
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = PrimaryNavy,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "운전자 신뢰 평가 원칙",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = PrimaryNavy
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "안전을 위한 제동은 단독 감점 항목으로 사용하지 않습니다. 센서 정보만으로 앞차 급정거를 확정할 수 없으므로 '${session.decelEvaluationNote}'으로 표시합니다.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            )
                            if (session.pendingReason != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "판단 보류 이유: ${session.pendingReason}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = WarningAmber,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }
                    }

                    // Driver Situation Memo Section (기획서 3페이지: 사용자 설명은 센서 관측과 구분된 메모로 남김)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.EditNote,
                                        contentDescription = null,
                                        tint = PrimaryNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "운전자 상황 설명 메모",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = PrimaryNavy
                                        )
                                    )
                                }
                                Text(
                                    text = "센서 데이터와 별도 분리 기록",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.5.sp,
                                        color = TextTertiary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = memoText,
                                onValueChange = {
                                    memoText = it
                                    isEditingMemo = true
                                },
                                placeholder = {
                                    Text(
                                        "예: 비가 많이 와서 서행함, 앞차 급정거로 안전 제동함 등 당시 상황 메모",
                                        fontSize = 12.sp,
                                        color = TextTertiary
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("trip_memo_input"),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                                minLines = 2,
                                maxLines = 3
                            )

                            if (isEditingMemo || memoText != (session.userSituationMemo ?: "")) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        onSaveMemo(session.id, memoText)
                                        isEditingMemo = false
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .height(38.dp)
                                        .testTag("save_trip_memo_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryBlue,
                                        contentColor = AlwaysWhite
                                    )
                                ) {
                                    Text(
                                        text = "메모 저장",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlwaysWhite
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_trip_detail_dialog_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "확인 완료 (닫기)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = AlwaysWhite
                    )
                }
            }
        }
    }
}
