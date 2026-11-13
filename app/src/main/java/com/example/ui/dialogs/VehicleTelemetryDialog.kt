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
import com.example.data.model.VehicleProfile
import com.example.ui.components.StatusChip
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VehicleTelemetryDialog(
    vehicle: VehicleProfile,
    onDismiss: () -> Unit
) {
    var isRunningDiagnostic by remember { mutableStateOf(false) }
    var diagnosticDone by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("vehicle_telemetry_dialog"),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ChipSensorBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = ChipSensorText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "차량 센서 & 실시간 진단",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Vehicle SDK 실시간 CAN 텔레매틱스",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_telemetry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = SurfaceBorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // SDK Live Status Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChipVerifiedBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(AccentGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Pleos Connect CAN 네트워크 정상",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ChipVerifiedText
                                        )
                                    )
                                    Text(
                                        text = "통신 지연: 38ms · 패킷 손실: 0%",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                            }
                            StatusChip(
                                text = "정상 수신 중",
                                backgroundColor = CleanWhite,
                                textColor = AccentGreen
                            )
                        }
                    }

                    // 4-Wheel TPMS Tire Pressure Visualizer
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "타이어 공기압 센서 (TPMS)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = "권장: 35~37 psi",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        color = TextTertiary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 4 wheels grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Front Left
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("전륜 좌측 (FL)", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("36.2 psi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryNavy)
                                    Text("정상 (23°C)", fontSize = 10.5.sp, color = AccentTeal)
                                }
                                // Front Right
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("전륜 우측 (FR)", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("36.0 psi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryNavy)
                                    Text("정상 (23°C)", fontSize = 10.5.sp, color = AccentTeal)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Rear Left
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("후륜 좌측 (RL)", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("35.8 psi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryNavy)
                                    Text("정상 (22°C)", fontSize = 10.5.sp, color = AccentTeal)
                                }
                                // Rear Right
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("후륜 우측 (RR)", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("35.9 psi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryNavy)
                                    Text("정상 (22°C)", fontSize = 10.5.sp, color = AccentTeal)
                                }
                            }
                        }
                    }

                    // On-board Powertrain & Electrical Telemetry
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "파워트레인 & 전장 계통",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = PrimaryNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("12V 시동 배터리 전압", color = TextSecondary, fontSize = 12.5.sp)
                                Text("12.6 V (안정적)", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.5.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("하이브리드 고전압 배터리 SOC", color = TextSecondary, fontSize = 12.5.sp)
                                Text("62% (최적 충전)", fontWeight = FontWeight.Bold, color = AccentTeal, fontSize = 12.5.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("엔진 오일 레벨 센서", color = TextSecondary, fontSize = 12.5.sp)
                                Text("정상 (적정 유량 확인)", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.5.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("냉각수 온도", color = TextSecondary, fontSize = 12.5.sp)
                                Text("88 °C (정상 작동 범위)", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 12.5.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("DTC 고장 코드 진단", color = TextSecondary, fontSize = 12.5.sp)
                                Text("0건 감지 (Clean)", fontWeight = FontWeight.Bold, color = AccentGreen, fontSize = 12.5.sp)
                            }
                        }
                    }

                    // Self Diagnostic Button & Result
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isRunningDiagnostic) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = PrimaryBlue,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "CAN 버스 32개 제어기 자가진단 수행 중...",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = PrimaryNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            } else if (diagnosticDone) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = AccentGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "전체 전자제어시스템(ECU/MCU/BMS) 무결성 확인 완료",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = AccentGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            } else {
                                OutlinedButton(
                                    onClick = {
                                        isRunningDiagnostic = true
                                        coroutineScope.launch {
                                            delay(1200)
                                            isRunningDiagnostic = false
                                            diagnosticDone = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp)
                                        .testTag("run_diagnostics_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "실시간 센서 무결성 자가 진단 실행",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp
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
                        .testTag("dismiss_telemetry_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "닫기",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = AlwaysWhite
                        )
                    )
                }
            }
        }
    }
}
