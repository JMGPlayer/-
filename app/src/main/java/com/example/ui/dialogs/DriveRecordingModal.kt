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
import androidx.compose.runtime.Composable
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
import com.example.data.model.DrivingSession
import com.example.ui.theme.*

@Composable
fun DriveRecordingModal(
    isSimulating: Boolean,
    currentSpeedKmh: Int,
    distanceKm: Double,
    efficiencyKml: Double,
    durationSec: Int,
    finishedSession: DrivingSession?,
    onStartDrive: () -> Unit,
    onStopDrive: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(CleanWhite),
            color = CleanWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Fixed Top Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorder, CircleShape)
                            .testTag("close_drive_modal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = PrimaryNavy
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSimulating) ChipVerifiedBg else ChipSensorBg)
                            .border(
                                1.dp,
                                if (isSimulating) AccentGreen.copy(alpha = 0.3f) else PrimaryBlueBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isSimulating) AccentGreen else PrimaryBlue)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (finishedSession != null) "주행 완료 분석 리포트" else if (isSimulating) "실시간 주행 기록 중" else "주행 대기 모드",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSimulating) ChipVerifiedText else PrimaryNavy,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(44.dp))
                }

                // Scrollable Center Content (Flexible weight ensures bottom bar is always visible)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (finishedSession != null) {
                            // Finished Drive Analysis Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("post_drive_summary_card"),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(ChipVerifiedBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ChipVerifiedText,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "주행 종료 및 패턴 분석 완료",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = TextPrimary
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "반복 출퇴근 경로로 비교 분석 대상에 추가되었습니다",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 12.5.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SurfaceLight)
                                            .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(12.dp))
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("주행 거리", fontSize = 11.sp, color = TextTertiary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("${finishedSession.distanceKm} km", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                        }
                                        Box(modifier = Modifier.width(1.dp).height(28.dp).background(SurfaceBorder))
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(if (finishedSession.efficiencyUnit == "km/kWh") "평균 전비" else "평균 연비", fontSize = 11.sp, color = TextTertiary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("${finishedSession.efficiency} ${finishedSession.efficiencyUnit}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                                        }
                                        Box(modifier = Modifier.width(1.dp).height(28.dp).background(SurfaceBorder))
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("평균 속도", fontSize = 11.sp, color = TextTertiary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("${finishedSession.averageSpeedKmh} km/h", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ChipVerifiedBg)
                                            .border(1.dp, AccentGreen.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Insights,
                                            contentDescription = null,
                                            tint = ChipVerifiedText,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "탄력 주행 실천 성공! 동일 구간 평균 대비 +1.4 개선",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ChipVerifiedText,
                                                fontSize = 12.5.sp
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            // Live Minimal Distraction Driving HUD
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isSimulating) "$currentSpeedKmh" else "0",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 72.sp,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = "km/h",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(16.dp))
                                        .padding(vertical = 16.dp, horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("주행 거리", fontSize = 11.5.sp, color = TextTertiary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${String.format("%.1f", distanceKm)} km",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = TextPrimary
                                            )
                                        )
                                    }

                                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(SurfaceBorder))

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("실시간 연비/전비", fontSize = 11.5.sp, color = TextTertiary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${String.format("%.1f", efficiencyKml)}",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = PrimaryBlue
                                            )
                                        )
                                    }

                                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(SurfaceBorder))

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("주행 시간", fontSize = 11.5.sp, color = TextTertiary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val min = durationSec / 60
                                        val sec = durationSec % 60
                                        Text(
                                            text = String.format("%02d:%02d", min, sec),
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = TextPrimary
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Prominent Center Action Button for Starting / Stopping Recording
                                if (!isSimulating) {
                                    Button(
                                        onClick = onStartDrive,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(58.dp)
                                            .testTag("center_start_drive_button"),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = BrandNavyContainer,
                                            contentColor = AlwaysWhite
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryBlue),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = AlwaysWhite,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "실시간 주행 기록 시작하기",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = AlwaysWhite
                                                )
                                            )
                                            Text(
                                                text = "출발 시 터치 · 차량 센서 연동 자동 무결성 기록",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 11.5.sp,
                                                    color = AlwaysWhite.copy(alpha = 0.85f)
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                } else {
                                    Button(
                                        onClick = onStopDrive,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(58.dp)
                                            .testTag("center_stop_drive_button"),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = AlertRed,
                                            contentColor = AlwaysWhite
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stop,
                                            contentDescription = null,
                                            tint = AlwaysWhite,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "주행 종료 및 코칭 분석 보기",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = AlwaysWhite
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSimulating) SurfaceLight else PrimaryBlueSubtle)
                                        .border(
                                            1.dp,
                                            if (isSimulating) SurfaceBorderSubtle else PrimaryBlueBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 14.dp, vertical = 12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isSimulating) Icons.Default.Shield else Icons.Default.Info,
                                            contentDescription = null,
                                            tint = if (isSimulating) PrimaryNavy else PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (isSimulating)
                                                "안전 운행 우선: 주행 중에는 불필요한 알림을 차단하고 계기판 속도와 거리만 최소 간섭으로 기록합니다."
                                            else
                                                "출발 전: 가운데 [실시간 주행 기록 시작하기] 버튼을 누르면 센서 연동 기록이 시작됩니다.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (isSimulating) TextSecondary else PrimaryNavy,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Fixed Bottom Action Button (ALWAYS VISIBLE & NEVER CLIPPED)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {
                    if (finishedSession != null) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("close_drive_summary_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandButtonContainer,
                                contentColor = AlwaysWhite
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = null, tint = AlwaysWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "분석 완료 및 홈 화면으로 돌아가기",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = AlwaysWhite
                                )
                            )
                        }
                    } else if (isSimulating) {
                        Button(
                            onClick = onStopDrive,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("stop_drive_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AlertRed,
                                contentColor = AlwaysWhite
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Stop, contentDescription = null, tint = AlwaysWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "주행 종료 및 코칭 분석 보기",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = AlwaysWhite
                                )
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("modal_close_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                        ) {
                            Text(
                                text = "닫기",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
