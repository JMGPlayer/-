package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.model.CoachingGoal
import com.example.data.model.DataQualityType
import com.example.data.model.DrivingSession
import com.example.ui.components.MiniSpeedChart
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun DemoTripsCoachingScreen(
    sessions: List<DrivingSession>,
    coachingGoals: List<CoachingGoal>,
    activeFilter: String,
    onFilterChange: (String) -> Unit,
    onGoalSelect: (String) -> Unit,
    onTripClick: (DrivingSession) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredSessions = when (activeFilter) {
        "COMPARABLE" -> sessions.filter { it.isComparable && !it.isPendingJudgment }
        "PENDING" -> sessions.filter { it.isPendingJudgment }
        "GAP" -> sessions.filter { it.dataQuality != DataQualityType.VERIFIED_COMPLETE }
        else -> sessions
    }

    val activeGoal = coachingGoals.firstOrNull { it.isCurrentFocus }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CleanWhite),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Screen Title
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "주행 데이터 & AI 코칭",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        color = PrimaryNavy
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "비교 가능한 주행을 선별하고, 실천 과제 1개를 추적합니다",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.5.sp
                    )
                )
            }
        }

        // Active AI Coaching Goal Spotlight
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_coaching_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusChip(
                            text = "현재 AI 집중 과제",
                            backgroundColor = BrandNavyContainer,
                            textColor = AlwaysWhite
                        )
                        Text(
                            text = "${activeGoal?.progressCount ?: 0}/${activeGoal?.totalTargetTrips ?: 4}회 달성",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                fontSize = 12.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = activeGoal?.title ?: "출근길 부드러운 가속",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = activeGoal?.rationale ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ChipVerifiedBg)
                            .border(1.dp, AccentGreen.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ChipVerifiedText,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeGoal?.improvementSummary ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ChipVerifiedText,
                                fontSize = 12.5.sp
                            )
                        )
                    }
                }
            }
        }

        // Coaching Goal Switcher (Select ONE actionable task)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(
                    title = "AI 실천 과제 변경",
                    subtitle = "집중할 과제 1개를 선택하면 다음 주행부터 실천 여부를 추적합니다"
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(coachingGoals) { goal ->
                        val isSelected = goal.isCurrentFocus
                        Card(
                            modifier = Modifier
                                .width(250.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onGoalSelect(goal.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ChipSensorBg else CleanWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) PrimaryBlue else SurfaceBorder
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isSelected) "선택됨" else "선택 가능",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) PrimaryBlue else TextTertiary,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = goal.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp,
                                        color = TextPrimary
                                    ),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = goal.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    ),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter Bar for Driving Sessions
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(
                    title = "주행 세션 목록",
                    subtitle = "외부 조건이 다른 주행은 왜곡을 막기 위해 판단을 보류합니다"
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterItems = listOf(
                        "ALL" to "전체 (${sessions.size})",
                        "COMPARABLE" to "비교 가능 (출퇴근) (${sessions.count { it.isComparable && !it.isPendingJudgment }})",
                        "PENDING" to "판단 보류 (조건 상이) (${sessions.count { it.isPendingJudgment }})"
                    )

                    filterItems.forEach { (key, label) ->
                        val isSelected = activeFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) BrandButtonContainer else SurfaceLight)
                                .border(
                                    1.dp,
                                    if (isSelected) BrandButtonContainer else SurfaceBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { onFilterChange(key) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) AlwaysWhite else TextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                val filterExplanation = when (activeFilter) {
                    "COMPARABLE" -> "동일한 출퇴근 경로로 주행 환경이 유사하여, 운전 습관 개선에 따른 실질 연비 차이를 객관적으로 비교합니다."
                    "PENDING" -> "폭우·우회로·터널 구간 등 외부 조건 차이로 인해 불리한 감점을 방지하고자 공정하게 비교를 보류한 주행입니다."
                    else -> "모든 주행 세션을 표시합니다. 공정한 코칭을 위해 외부 조건이 상이한 주행은 비교에서 자동 제외됩니다."
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceLight)
                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = filterExplanation,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.5.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }
        }

        // Empty state when filtered list has no items
        if (filteredSessions.isEmpty()) {
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
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "해당 조건의 주행 기록이 아직 없습니다",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "필터에서 '전체'를 선택하거나, 새로운 출퇴근 주행을 기록해보세요.",
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

        // Sessions List Items
        items(filteredSessions) { session ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onTripClick(session) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = session.title,
                            style = MaterialTheme.typography.titleMedium.copy(
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
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (session.isComparable && !session.isPendingJudgment) {
                                StatusChip(
                                    text = "비교 가능",
                                    backgroundColor = ChipVerifiedBg,
                                    textColor = ChipVerifiedText
                                )
                            } else if (session.isPendingJudgment) {
                                StatusChip(
                                    text = "판단 보류",
                                    backgroundColor = ChipPendingBg,
                                    textColor = ChipPendingText
                                )
                            }
                            if (session.dataQuality == DataQualityType.PARTIAL_TUNNEL_GAP) {
                                StatusChip(
                                    text = "음영 누락 기록",
                                    backgroundColor = SurfaceLight,
                                    textColor = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${session.dateFormatted} · ${session.distanceKm} km · ${session.durationMinutes}분 소요",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Metrics Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (session.efficiencyUnit == "km/kWh") "실제 전비" else "실제 연비",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextTertiary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${session.efficiency} ${session.efficiencyUnit}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = if (session.isPendingJudgment) TextSecondary else PrimaryBlue
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "평균 속도",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextTertiary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${session.averageSpeedKmh} km/h",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = TextPrimary
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "데이터 출처",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextTertiary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "예시 데이터",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = PrimaryNavy
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Speed mini chart with area gradient
                    MiniSpeedChart(samples = session.speedSamples)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Decel & coaching note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = if (session.isPendingJudgment) WarningAmber else AccentTeal,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (session.isPendingJudgment) {
                                session.pendingReason ?: "조건 차이로 개선 효과 판단 보류"
                            } else {
                                session.decelEvaluationNote
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }

                    if (!session.userSituationMemo.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceLight)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "운전자 상황 메모: ${session.userSituationMemo}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = PrimaryNavy
                                ),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
