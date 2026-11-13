package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.PowerTrainType
import com.example.data.model.UsageCondition
import com.example.data.model.VehicleProfile
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun VehicleDetailModal(
    vehicle: VehicleProfile,
    registeredDrivers: List<String> = listOf(
        "김민준 (주 운전자 · 본인)",
        "이수진 (공동 운전자 · 배우자)",
        "김태호 (가족 · 주말 이용)"
    ),
    onSelectDriver: (String) -> Unit,
    onAddDriver: (String) -> Unit = onSelectDriver,
    onRemoveDriver: (String) -> Unit = {},
    onSelectCondition: (UsageCondition) -> Unit,
    onDismiss: () -> Unit
) {
    var customDriverInput by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("vehicle_detail_dialog"),
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
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "차량 등록 정보 & 연동 제원",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "AAOS CarPropertyManager & Vehicle SDK",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_vehicle_detail_button")
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Vehicle Core Specs Card
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
                                    text = vehicle.modelName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = TextPrimary
                                    )
                                )
                                StatusChip(
                                    text = vehicle.plateNumber,
                                    backgroundColor = BrandNavyContainer,
                                    textColor = AlwaysWhite
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("연식 및 모델", fontSize = 12.5.sp, color = TextTertiary)
                                Text("${vehicle.modelYear}년식", fontWeight = FontWeight.Bold, fontSize = 12.5.sp, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("동력계 유형", fontSize = 12.5.sp, color = TextTertiary)
                                Text(vehicle.fuelType, fontWeight = FontWeight.Bold, fontSize = 12.5.sp, color = PrimaryBlue)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("공인 표준 효율", fontSize = 12.5.sp, color = TextTertiary)
                                Text(vehicle.officialEfficiency, fontWeight = FontWeight.Bold, fontSize = 12.5.sp, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("누적 주행거리", fontSize = 12.5.sp, color = TextTertiary)
                                Text("${String.format("%,d", vehicle.currentMileageKm)} km", fontWeight = FontWeight.ExtraBold, fontSize = 13.5.sp, color = PrimaryNavy)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("마지막 갱신 시각", fontSize = 12.5.sp, color = TextTertiary)
                                Text(vehicle.lastSyncTime, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = AccentTeal)
                            }

                            if (vehicle.powerTrainType == PowerTrainType.EV) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = SurfaceBorderSubtle)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("고전압 배터리 잔량 (SOC)", fontSize = 12.5.sp, color = TextTertiary)
                                    Text("${vehicle.batterySocPercent ?: 74}%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentTeal)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("배터리 건강도 (SOH)", fontSize = 12.5.sp, color = TextTertiary)
                                    Text("${vehicle.batterySohPercent ?: 99}% (양호)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGreen)
                                }
                            }
                        }
                    }

                    // Shared Vehicle Driver Selector (공동 이용 차량 운전자 선택)
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
                                Column {
                                    Text(
                                        text = "공동 이용 차량 운전자 설정",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.5.sp,
                                            color = PrimaryNavy
                                        )
                                    )
                                    Text(
                                        text = "주차 상태에서 운전자를 선택하여 개인 습관을 분리 분석합니다",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val displayDrivers = if (registeredDrivers.contains(vehicle.currentDriver)) {
                                registeredDrivers
                            } else {
                                registeredDrivers + vehicle.currentDriver
                            }

                            displayDrivers.forEach { driver ->
                                val isSelected = vehicle.currentDriver == driver
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ChipSensorBg else SurfaceLight)
                                        .border(
                                            1.dp,
                                            if (isSelected) PrimaryBlue else SurfaceBorderSubtle,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { onSelectDriver(driver) }
                                        .padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = driver,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) PrimaryNavy else TextPrimary,
                                            fontSize = 13.sp
                                        ),
                                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "선택됨",
                                                tint = PrimaryBlue,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        if (displayDrivers.size > 1 && !isSelected) {
                                            IconButton(
                                                onClick = { onRemoveDriver(driver) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "운전자 삭제",
                                                    tint = TextTertiary,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(color = SurfaceBorderSubtle)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "새 운전자 직접 입력 및 등록",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = customDriverInput,
                                    onValueChange = { customDriverInput = it },
                                    placeholder = {
                                        Text(
                                            text = "예: 박서준 (가족 · 주말 이용)",
                                            fontSize = 12.sp,
                                            color = TextTertiary
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("custom_driver_input")
                                )
                                Button(
                                    onClick = {
                                        val trimmed = customDriverInput.trim()
                                        if (trimmed.isNotEmpty()) {
                                            onAddDriver(trimmed)
                                            customDriverInput = ""
                                        }
                                    },
                                    enabled = customDriverInput.isNotBlank(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryBlue,
                                        contentColor = AlwaysWhite
                                    ),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("add_custom_driver_button")
                                ) {
                                    Text(
                                        text = "등록",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AlwaysWhite
                                    )
                                }
                            }
                        }
                    }

                    // Driving Condition Mode (통상 조건 vs 가혹 조건)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "주행 환경 및 관리 기준 선택",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "공식 취급설명서 기준: 가혹 주행 시 교체 주기가 50% 단축 적용됩니다",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.5.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                UsageCondition.values().forEach { cond ->
                                    val isSelected = vehicle.usageCondition == cond
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) ChipSensorBg else SurfaceLight)
                                            .border(
                                                1.5.dp,
                                                if (isSelected) PrimaryBlue else SurfaceBorderSubtle,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { onSelectCondition(cond) }
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = cond.label,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) PrimaryNavy else TextPrimary,
                                                fontSize = 13.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = cond.description,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.5.sp,
                                                color = TextSecondary,
                                                lineHeight = 14.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Vehicle SDK Supported Sensor Capabilities Checklist
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "차량 연동 데이터 지원 항목 (Capabilities)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val capabilities = listOf(
                                "계기판 차속 (PERF_VEHICLE_SPEED)" to "지원 (실시간)",
                                "누적 주행거리 (PERF_ODOMETER)" to "지원 (실시간)",
                                "순간/구간 연비 및 전비" to "동일 구간 신뢰 집계",
                                "4륜 개별 공기압 (TIRE_PRESSURE)" to "지원 (정상 36psi)",
                                "12V 배터리 전압 및 EV SOC" to "지원 (실시간)",
                                "DTC 고장 진단 코드" to "지원 (0건 감지)"
                            )

                            capabilities.forEach { (item, status) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item, fontSize = 11.5.sp, color = TextPrimary)
                                    Text(status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentTeal)
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
                        .testTag("dismiss_vehicle_detail_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "정보 확인 완료 (닫기)",
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
