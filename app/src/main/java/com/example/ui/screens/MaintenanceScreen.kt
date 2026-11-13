package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.MaintenanceActionType
import com.example.data.model.MaintenanceRecord
import com.example.data.model.MaintenanceScheduleItem
import com.example.data.model.UsageCondition
import com.example.data.model.VehicleProfile
import com.example.data.model.VerificationLevel
import com.example.ui.components.ConsumableProgress
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun MaintenanceScreen(
    vehicle: VehicleProfile,
    schedules: List<MaintenanceScheduleItem>,
    records: List<MaintenanceRecord>,
    onRegisterMaintenanceClick: () -> Unit,
    onRecordClick: (MaintenanceRecord) -> Unit = {},
    onSelectCondition: (UsageCondition) -> Unit = {},
    onOpenManualClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CleanWhite),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "정비 증빙 & 관리 일정",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = PrimaryNavy
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "공식 관리 기준과 확인된 영수증 증빙으로 정비 일정을 관리합니다",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.5.sp
                    )
                )
            }
        }

        // Usage Condition Switcher Card (통상 조건 vs 가혹 조건 동적 스위처)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("usage_condition_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "주행 환경 관리 기준",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy
                                )
                            )
                        }
                        StatusChip(
                            text = if (vehicle.usageCondition == UsageCondition.SEVERE) "가혹 조건" else "통상 표준",
                            backgroundColor = if (vehicle.usageCondition == UsageCondition.SEVERE) WarningAmberLight else ChipSensorBg,
                            textColor = if (vehicle.usageCondition == UsageCondition.SEVERE) WarningAmber else ChipSensorText
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UsageCondition.values().forEach { cond ->
                            val isSelected = vehicle.usageCondition == cond
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSelectCondition(cond) }
                                    .testTag("condition_chip_${cond.name.lowercase()}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) BrandNavyContainer else SurfaceLight
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) BrandNavyContainer else SurfaceBorderSubtle
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = cond.label,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isSelected) AlwaysWhite else TextPrimary
                                            )
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = AlwaysWhite,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = if (cond == UsageCondition.SEVERE) "정체·단거리 50% 단축" else "제조사 표준 권장 주기",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.5.sp,
                                            color = if (isSelected) AlwaysWhite.copy(alpha = 0.85f) else TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Register Receipt Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_maintenance_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlueSubtle),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DocumentScanner,
                                contentDescription = null,
                                tint = AlwaysWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "정비 명세서 / 영수증 등록",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "명세서 항목 자동 추출 후 사용자 확인을 거쳐 반영됩니다",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onRegisterMaintenanceClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("open_maintenance_dialog_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = AlwaysWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = AlwaysWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "새 정비 내역 증빙 등록하기",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AlwaysWhite
                            )
                        )
                    }
                }
            }
        }

        // Maintenance Rule Disclaimer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentTealLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = AccentTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "센서 데이터만으로 부품의 마모나 고장을 단정하지 않으며, 제조사 공식 관리 주기와 증빙된 정비 이력을 기준으로 안내합니다.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Consumable Official Schedules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "소모품 관리 기준 & 교체 주기",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "현재 누적 주행거리: ${String.format("%,d", vehicle.currentMileageKm)} km",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }

                Surface(
                    onClick = onOpenManualClick,
                    shape = RoundedCornerShape(10.dp),
                    color = ChipSensorBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                    modifier = Modifier.testTag("open_manual_badge_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "현대차 공식 매뉴얼",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }
        }

        items(schedules) { schedule ->
            ConsumableProgress(
                name = schedule.name,
                lastServicedKm = schedule.lastServicedKm,
                nextDueKm = schedule.nextDueKm,
                currentMileageKm = vehicle.currentMileageKm,
                officialIntervalKm = schedule.officialIntervalKm,
                statusLabel = schedule.status.label
            )
        }

        // Verified Maintenance Records Timeline
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "확인된 정비 증빙 이력",
                subtitle = "문서로 뒷받침되는 공식 관리 내역입니다",
                badgeText = "증빙 첨부 100%",
                badgeColor = ChipVerifiedBg,
                badgeTextColor = ChipVerifiedText
            )
        }

        if (records.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SurfaceLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "아직 등록된 정비 영수증이 없습니다",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "블루핸즈나 일반 정비소에서 받은 영수증을 등록하면 공인 정비 이력으로 안전하게 보관됩니다.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.5.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        )
                    }
                }
            }
        }

        items(records) { record ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onRecordClick(record) }
                    .testTag("maintenance_record_card_${record.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = record.workshopName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.5.sp,
                                color = TextPrimary
                            ),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (actionBg, actionText) = when (record.actionType) {
                                MaintenanceActionType.INSPECTION -> AccentTealLight to AccentTeal
                                MaintenanceActionType.REPLENISHMENT -> PrimaryBlueSubtle to PrimaryBlue
                                MaintenanceActionType.REPLACEMENT -> ChipSensorBg to PrimaryNavy
                            }
                            StatusChip(
                                text = record.actionType.label,
                                backgroundColor = actionBg,
                                textColor = actionText
                            )
                            StatusChip(
                                text = record.verificationLevel.label,
                                backgroundColor = if (record.isDocumentVerified) ChipVerifiedBg else SurfaceLight,
                                textColor = if (record.isDocumentVerified) ChipVerifiedText else TextTertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${record.dateFormatted} · ${String.format("%,d", record.mileageKm)} km 당시 점검",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Work items list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        record.items.forEach { itemText ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlue)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = itemText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 12.5.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "첨부: ${record.documentTitle}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextTertiary,
                                fontSize = 12.sp
                            ),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = if (record.totalCostWon > 0) "${String.format("%,d", record.totalCostWon)}원" else "무상 점검",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                    }
                }
            }
        }
    }
}
