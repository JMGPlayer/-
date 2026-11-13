package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CoachingGoal
import com.example.data.model.DrivingSession
import com.example.data.model.VehicleProfile
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab

@Composable
fun HomeScreen(
    vehicle: VehicleProfile,
    activeGoal: CoachingGoal?,
    recentTrip: DrivingSession?,
    garageVehicles: List<VehicleProfile> = emptyList(),
    loggedInUserName: String = "김민준",
    onSelectVehicle: (String) -> Unit = {},
    onAddVehicleClick: () -> Unit = {},
    onStartDriveClick: () -> Unit,
    onRegisterMaintenanceClick: () -> Unit,
    onNavigateTab: (AppTab) -> Unit,
    onTripClick: (DrivingSession) -> Unit,
    onTelemetryClick: () -> Unit = {},
    onDemoScenarioClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onOpenDetailedGuideClick: () -> Unit = {},
    onVehicleCardClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CleanWhite),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // App Top Bar Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Car Value Coach",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = PrimaryNavy
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Demo Scenario Switcher Button
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SurfaceLight)
                                .border(0.8.dp, SurfaceBorder, CircleShape)
                                .clickable { onDemoScenarioClick() }
                                .testTag("open_scenario_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "시나리오 전환",
                                tint = PrimaryNavy,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // App Settings Button (환경설정 & 다크모드)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SurfaceLight)
                                .border(0.8.dp, SurfaceBorder, CircleShape)
                                .clickable { onSettingsClick() }
                                .testTag("open_settings_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "환경설정",
                                tint = PrimaryNavy,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${loggedInUserName}님 · 차량 데이터 주행 코칭 & 투명 이력",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        maxLines = 2,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp)
                    )

                    // Vehicle SDK Telemetry Chip (Clickable)
                    Surface(
                        onClick = onTelemetryClick,
                        shape = RoundedCornerShape(20.dp),
                        color = ChipSensorBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                        modifier = Modifier.testTag("telemetry_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(AccentGreen)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "센서 진단",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ChipSensorText,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
            }
        }

        // Beginner Quick Guide Card (Placed at the VERY TOP for immediate discoverability)
        item {
            var isGuideExpanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("beginner_guide_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlueSubtle),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isGuideExpanded = !isGuideExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = AlwaysWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "처음 오셨나요? 3단계 핵심 이용법",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = PrimaryNavy
                                )
                            )
                        }
                        Icon(
                            imageVector = if (isGuideExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isGuideExpanded) "접기" else "펼치기",
                            tint = PrimaryNavy
                        )
                    }

                    if (isGuideExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = PrimaryBlueBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text("1.", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "출발 전: 아래 [실시간 주행 기록 시작하기] 버튼을 누르면 속도와 연비가 센서로 안전하게 자동 기록됩니다.",
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.sp,
                                    color = TextPrimary
                                )
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Text("2.", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "주행 중: AI 코치가 제안한 단 1개의 꿀팁(탄력 주행 등)을 편안하게 실천해보세요.",
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.sp,
                                    color = TextPrimary
                                )
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Text("3.", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "정비 후: 정비 영수증을 등록하면 나중에 차량 판매 시 믿을 수 있는 '투명 가치 리포트'가 완성됩니다.",
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        onClick = onOpenDetailedGuideClick,
                        shape = RoundedCornerShape(10.dp),
                        color = SurfacePureWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_detailed_guide_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "가이드 자세하게 보기 (앱 소개 · 단계별 사용법)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = PrimaryBlue
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "상세 가이드 열기",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // Family Garage (Multi-Vehicle Selector)
        if (garageVehicles.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("garage_selector_container"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlueSubtle),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Garage,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "내 차고 (보유 차량 관리)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = PrimaryNavy
                                    )
                                )
                            }
                            TextButton(
                                onClick = onAddVehicleClick,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp), tint = PrimaryBlue)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("차량 추가 등록", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(garageVehicles) { v ->
                                val isSelected = v.id == vehicle.id
                                val thumbRes = when {
                                    v.id == "veh_ioniq5" || v.powerTrainType == com.example.data.model.PowerTrainType.EV -> R.drawable.img_ioniq_isolated_1794394301856
                                    v.id == "veh_genesis" -> R.drawable.img_genesis_isolated_1794394316483
                                    else -> R.drawable.img_sonata_isolated_1794394286280
                                }

                                Card(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onSelectVehicle(v.id) }
                                        .testTag("garage_chip_${v.id}"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) BrandNavyContainer else SurfaceLight
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) PrimaryBlue else SurfaceBorder
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Miniature Real Car Cutout Thumbnail
                                        Image(
                                            painter = painterResource(id = thumbRes),
                                            contentDescription = v.modelName,
                                            modifier = Modifier
                                                .size(width = 44.dp, height = 28.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(AlwaysWhite)
                                                .border(0.5.dp, if (isSelected) AlwaysWhite.copy(alpha = 0.35f) else SurfaceBorder, RoundedCornerShape(6.dp))
                                                .padding(2.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = v.nickname.ifBlank { v.modelName },
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp,
                                                    color = if (isSelected) AlwaysWhite else TextPrimary
                                                )
                                            )
                                            Text(
                                                text = "${v.plateNumber} · ${v.currentDriver.split(" ").first()}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 10.5.sp,
                                                    color = if (isSelected) AlwaysWhite.copy(alpha = 0.85f) else TextSecondary
                                                )
                                            )
                                        }
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "선택됨",
                                                tint = AccentGreen,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Vehicle Hero Card (Ultra-refined with 3-spec dashboard)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onVehicleCardClick() }
                    .testTag("vehicle_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    val realCarPhotoRes = when {
                        vehicle.id == "veh_ioniq5" || vehicle.powerTrainType == com.example.data.model.PowerTrainType.EV -> {
                            R.drawable.img_ioniq_isolated_1794394301856
                        }
                        vehicle.id == "veh_genesis" -> {
                            R.drawable.img_genesis_isolated_1794394316483
                        }
                        else -> {
                            R.drawable.img_sonata_isolated_1794394286280
                        }
                    }

                    // Vehicle Image Banner - Pure White Showroom Cutout
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                            .background(AlwaysWhite)
                    ) {
                        Image(
                            painter = painterResource(id = realCarPhotoRes),
                            contentDescription = "${vehicle.modelName} 실제 차량 이미지",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(165.dp)
                                .align(Alignment.Center)
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            contentScale = ContentScale.Fit
                        )

                        // Clean License Plate Pill & Vehicle Nickname Pill (White Text)
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1호차 / Nickname Badge with White Text
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandDarkNavy)
                                    .border(1.dp, Color(0xFF2A3E5C), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = vehicle.nickname,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        color = AlwaysWhite
                                    )
                                )
                            }

                            // License Plate Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AlwaysWhite.copy(alpha = 0.96f))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = vehicle.plateNumber,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = BrandDarkNavy
                                    )
                                )
                            }

                            // Driver Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandDarkNavy.copy(alpha = 0.88f))
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = vehicle.currentDriver.split(" ").first(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = AlwaysWhite
                                    )
                                )
                            }
                        }

                        // Condition badge (Top End)
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (vehicle.usageCondition == com.example.data.model.UsageCondition.SEVERE) WarningAmber.copy(alpha = 0.92f) else Color(0xFFF0F6FF))
                                .border(1.dp, if (vehicle.usageCondition == com.example.data.model.UsageCondition.SEVERE) WarningAmber else Color(0xFFCCE0FF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = vehicle.usageCondition.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (vehicle.usageCondition == com.example.data.model.UsageCondition.SEVERE) AlwaysWhite else Color(0xFF1D4ED8)
                                )
                            )
                        }
                    }

                    // Section Divider separating photo banner from vehicle specs
                    HorizontalDivider(color = SurfaceBorder, thickness = 1.dp)

                    // Vehicle Profile Details
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = vehicle.modelName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${vehicle.modelYear}년식 · ${vehicle.fuelType}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.5.sp
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${String.format("%,d", vehicle.currentMileageKm)} km",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = "공인 ${vehicle.officialEfficiency}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextTertiary,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // 3-Spec Micro Grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceLight)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("주행거리", fontSize = 11.sp, color = TextTertiary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "${String.format("%,d", vehicle.currentMileageKm)} km",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                            }
                            Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceBorder))
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val effLabel = if (vehicle.powerTrainType == com.example.data.model.PowerTrainType.EV) "최근 평균 전비" else "최근 평균 연비"
                                val effUnit = vehicle.powerTrainType.efficiencyUnit
                                val defaultEff = if (vehicle.powerTrainType == com.example.data.model.PowerTrainType.EV) 6.2 else 19.8
                                Text(effLabel, fontSize = 11.sp, color = TextTertiary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "${recentTrip?.efficiency ?: defaultEff} $effUnit",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryBlue
                                )
                            }
                            Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceBorder))
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (vehicle.powerTrainType == com.example.data.model.PowerTrainType.EV) {
                                    Text("배터리 SOC", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "${vehicle.batterySocPercent ?: 74}% (정상)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AccentGreen
                                    )
                                } else {
                                    Text("데이터 무결성", fontSize = 11.sp, color = TextTertiary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "96.8% (정상)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = AccentGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Drive Action Hero Button
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onStartDriveClick() }
                    .testTag("start_drive_button"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = BrandNavyContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AlwaysWhite.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = AlwaysWhite,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "실시간 주행 기록 시작하기 (출발할 때 터치)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = AlwaysWhite
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "운전 중 조작 불필요 · 도착 후 '종료'만 누르면 자동 저장",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AlwaysWhite.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "시작",
                            tint = AlwaysWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // AI Coaching Single Focus Goal Card
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(
                    title = "AI 코치 오늘의 실천 미션",
                    subtitle = "무리한 복수 지표 대신, 오늘 집중할 단 하나의 운전 꿀팁입니다",
                    badgeText = "오늘의 1개 집중",
                    badgeColor = ChipSensorBg,
                    badgeTextColor = ChipSensorText
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (activeGoal != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateTab(AppTab.TRIPS_COACHING) }
                            .testTag("coaching_goal_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusChip(
                                    text = "오늘의 실천 미션",
                                    backgroundColor = ChipVerifiedBg,
                                    textColor = ChipVerifiedText
                                )
                                Text(
                                    text = "목표 달성 ${activeGoal.progressCount}/${activeGoal.totalTargetTrips}회",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp,
                                        color = PrimaryBlue
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = activeGoal.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = activeGoal.targetAction,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary,
                                    fontSize = 13.5.sp,
                                    lineHeight = 19.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Linear Progress Bar
                            val progress = (activeGoal.progressCount.toFloat() / activeGoal.totalTargetTrips).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(CircleShape),
                                color = AccentTeal,
                                trackColor = SurfaceSubtle
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SurfaceLight)
                                    .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                        contentDescription = null,
                                        tint = AccentTeal,
                                        modifier = Modifier.size(17.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = activeGoal.improvementSummary,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary,
                                            fontSize = 12.5.sp
                                        )
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "상세보기",
                                    tint = TextTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Data Transparency Principle Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "운전자가 안심할 수 있는 3가지 신뢰 원칙",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = TextPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("①", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("안전을 위한 브레이크는 감점하지 않아요", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("앞차 급정차나 돌발 상황 회피 등 안전 제동을 결함으로 보지 않습니다.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.5.sp)
                            }
                        }
                        HorizontalDivider(color = SurfaceBorderSubtle, thickness = 0.5.dp)
                        Row(verticalAlignment = Alignment.Top) {
                            Text("②", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("터널 등 통신 끊김은 가짜 데이터로 꾸며내지 않아요", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("누락된 값은 임의 추정 없이 원형 그대로 정직하게 남깁니다.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.5.sp)
                            }
                        }
                        HorizontalDivider(color = SurfaceBorderSubtle, thickness = 0.5.dp)
                        Row(verticalAlignment = Alignment.Top) {
                            Text("③", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("폭우나 극심한 정체는 운전자 탓이 아니에요", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("기상 악화로 연비가 떨어진 주행은 공정하게 비교 평가를 보류합니다.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.5.sp)
                            }
                        }
                    }
                }
            }
        }

        // Recent Drive Preview & Action
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "최근 주행 기록",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    TextButton(
                        onClick = { onNavigateTab(AppTab.TRIPS_COACHING) }
                    ) {
                        Text(
                            text = "전체보기",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        )
                    }
                }

                if (recentTrip != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onTripClick(recentTrip) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = recentTrip.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.5.sp,
                                            color = TextPrimary
                                        ),
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (recentTrip.isComparable) {
                                        StatusChip(
                                            text = "비교 가능",
                                            backgroundColor = ChipVerifiedBg,
                                            textColor = ChipVerifiedText
                                        )
                                    } else if (recentTrip.isPendingJudgment) {
                                        StatusChip(
                                            text = "판단 보류",
                                            backgroundColor = ChipPendingBg,
                                            textColor = ChipPendingText
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${recentTrip.dateFormatted} · ${recentTrip.distanceKm}km · ${recentTrip.durationMinutes}분 소요",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${recentTrip.efficiency} km/L",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = "평균 ${recentTrip.averageSpeedKmh}km/h",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextTertiary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("empty_recent_trips_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = TextTertiary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "아직 기록된 주행이 없습니다",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "상단의 [지금 주행 시작하기] 버튼을 터치하여 첫 번째 출퇴근 주행을 기록해보세요.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }

        // Secondary Action Button (Register maintenance receipt)
        item {
            OutlinedButton(
                onClick = onRegisterMaintenanceClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("register_receipt_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CleanWhite,
                    contentColor = PrimaryNavy
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = PrimaryNavy
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "정비 영수증 / 명세서 등록",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}
