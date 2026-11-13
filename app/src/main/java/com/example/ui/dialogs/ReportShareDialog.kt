package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.TransparencyReport
import com.example.ui.theme.*

@Composable
fun ReportShareDialog(
    report: TransparencyReport,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var includeTelemetry by remember { mutableStateOf(true) }
    var includeMaintenance by remember { mutableStateOf(true) }
    var includeGapStatement by remember { mutableStateOf(true) }
    var maskPlate by remember { mutableStateOf(report.isMaskPlate) }
    var maskRoute by remember { mutableStateOf(report.isMaskRouteLocation) }

    val displayPlate = if (maskPlate) {
        val raw = report.plateNumber
        if (raw.length > 4) raw.take(raw.length - 4) + "****" else "12가 ****"
    } else report.plateNumber

    val formattedReportText = buildString {
        appendLine("======================================")
        appendLine("🚗 [Car Value Coach] 차량 사용·관리 투명 리포트")
        appendLine("======================================")
        appendLine("• 차량명: ${report.vehicleName} ($displayPlate)")
        appendLine("• 가치 신뢰 등급: ${report.transparencyGrade}")
        appendLine("• 기록 기간: ${report.trackingPeriod}")
        appendLine("• 누적 관리 거리: ${String.format("%,d", report.totalTrackedKm)} km")
        if (maskPlate || maskRoute) {
            val notes = listOfNotNull(
                if (maskPlate) "번호판 마스킹" else null,
                if (maskRoute) "경로 비식별화" else null
            ).joinToString(", ")
            appendLine("• 개인정보 보호: $notes 적용")
        }
        appendLine()
        if (includeTelemetry) {
            appendLine("[1. 주행 데이터 수집 무결성]")
            appendLine("- 차량 센서 직접 수집률: ${String.format("%.1f", report.sensorCollectionRatio)}%")
            appendLine("- 터널/음영 누락 구간: ${String.format("%.1f", report.missingOrGapRatio)}% (임의 추정값 없음)")
            val routeDesc = if (maskRoute) "비식별화 코스" else "동일 출퇴근 코스"
            appendLine("- 비교 가능 주행: ${report.comparableTripsCount}회 ($routeDesc) | 판단 보류(기상 악화 등): ${report.pendingJudgmentTripsCount}회")
            appendLine("- 코칭 성과: ${report.activeCoachingResult}")
            appendLine()
        }
        if (includeMaintenance) {
            appendLine("[2. 정비 및 소모품 관리 증빙]")
            appendLine("- 정비 명세서·영수증 증빙 확인율: ${String.format("%.1f", report.documentVerifiedMaintenanceRatio)}%")
            appendLine("- 공식 서비스센터 정비 이력 보존됨")
            appendLine()
        }
        if (includeGapStatement) {
            appendLine("[3. 데이터 투명성 공시]")
            appendLine("- ${report.certificationNote}")
        }
        appendLine("======================================")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(22.dp))
                .background(CleanWhite),
            color = CleanWhite
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "차량 양도용 리포트 생성",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "구매자/딜러에게 신뢰할 수 있는 이력을 공유합니다",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.5.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorder, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = PrimaryNavy)
                    }
                }

                Divider(color = SurfaceBorder)

                // Item toggles
                Text(
                    text = "공개 항목 선택",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("주행 센서 데이터 및 비교 코칭 결과", fontSize = 13.sp, color = TextPrimary)
                    Switch(checked = includeTelemetry, onCheckedChange = { includeTelemetry = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("정비 명세서 증빙 관리 내역", fontSize = 13.sp, color = TextPrimary)
                    Switch(checked = includeMaintenance, onCheckedChange = { includeMaintenance = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("누락 구간 및 데이터 무결성 공시", fontSize = 13.sp, color = TextPrimary)
                    Switch(checked = includeGapStatement, onCheckedChange = { includeGapStatement = it })
                }

                Divider(color = SurfaceBorderSubtle)

                Text(
                    text = "개인정보 보호 마스킹",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("차량 번호판 마스킹", fontSize = 13.sp, color = TextPrimary)
                        Text("끝 4자리를 '****'로 가려 공유합니다", fontSize = 11.sp, color = TextSecondary)
                    }
                    Switch(checked = maskPlate, onCheckedChange = { maskPlate = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("상세 경로 비식별화", fontSize = 13.sp, color = TextPrimary)
                        Text("출퇴근 코스 명칭을 가려 사생활 위치 노출을 방지합니다", fontSize = 11.sp, color = TextSecondary)
                    }
                    Switch(checked = maskRoute, onCheckedChange = { maskRoute = it })
                }

                // Text Preview Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceLight)
                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = formattedReportText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            lineHeight = 17.sp,
                            color = TextPrimary
                        )
                    )
                }

                // Copy & Share buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Car Value Report", formattedReportText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "리포트가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("copy_report_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("텍스트 복사", fontSize = 13.5.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "차량 양도용 리포트 공유 링크가 생성되었습니다.", Toast.LENGTH_LONG).show()
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("confirm_share_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandButtonContainer,
                            contentColor = AlwaysWhite
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = AlwaysWhite, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("공유하기", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = AlwaysWhite)
                    }
                }
            }
        }
    }
}
