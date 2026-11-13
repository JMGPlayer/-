package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.TransparencyReport
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun ValueReportScreen(
    report: TransparencyReport,
    onShareReportClick: () -> Unit,
    onToggleMaskPlate: () -> Unit = {},
    onToggleMaskRoute: () -> Unit = {},
    onOpenGradeGuidelineClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CleanWhite),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title & Export Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                ) {
                    Text(
                        text = "차량 가치 & 투명 리포트",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = PrimaryNavy
                        ),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "차량 양도 시 설명 자료로 활용 가능한 인증 리포트",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        ),
                        maxLines = 1
                    )
                }

                Surface(
                    onClick = onShareReportClick,
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryBlueSubtle,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                    modifier = Modifier.testTag("top_share_report_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "공유",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "공유",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = PrimaryBlue,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        // Beginner Explanation Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlueSubtle),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AlwaysWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "💡 이 리포트는 어떤 가치가 있나요?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "차량 양도 시 단순 계기판 숫자 대신 센서 직접 수집률과 공식 정비 영수증으로 교차 검증된 투명한 관리 이력을 증명할 수 있습니다.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        )
                    }
                }
            }
        }

        // Privacy Protection Masking Card (개인정보 보호 마스킹 제어)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("privacy_masking_card"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PrivacyTip,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "리포트 공유 시 개인정보 보호 마스킹",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SurfaceBorderSubtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Mask plate button
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onToggleMaskPlate() }
                                .testTag("toggle_mask_plate_button"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (report.isMaskPlate) ChipSensorBg else SurfaceLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (report.isMaskPlate) PrimaryBlue else SurfaceBorderSubtle
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (report.isMaskPlate) "✓ 번호판 마스킹 켜짐" else "번호판 원본 노출",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (report.isMaskPlate) PrimaryNavy else TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (report.isMaskPlate) "12가 **** 로 가림" else report.plateNumber,
                                    fontSize = 11.sp,
                                    color = if (report.isMaskPlate) PrimaryBlue else TextTertiary
                                )
                            }
                        }

                        // Mask route button
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onToggleMaskRoute() }
                                .testTag("toggle_mask_route_button"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (report.isMaskRouteLocation) ChipSensorBg else SurfaceLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (report.isMaskRouteLocation) PrimaryBlue else SurfaceBorderSubtle
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (report.isMaskRouteLocation) "✓ 경로 비식별화 켜짐" else "경로 원본 노출",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (report.isMaskRouteLocation) PrimaryNavy else TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (report.isMaskRouteLocation) "출퇴근 명칭 비식별화" else "출퇴근 상세 코스",
                                    fontSize = 11.sp,
                                    color = if (report.isMaskRouteLocation) PrimaryBlue else TextTertiary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Trust Grade Certificate Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_certificate_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BrandNavyContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AlwaysWhite.copy(alpha = 0.14f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Pleos Connect 공인 이력",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AlwaysWhite,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = report.transparencyGrade,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 27.sp,
                                    color = AlwaysWhite
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${report.vehicleName} · ${report.plateNumber}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = AlwaysWhite.copy(alpha = 0.85f),
                                    fontSize = 13.sp
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AccentTeal.copy(alpha = 0.28f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = AlwaysWhite,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Visual Grade Criteria Matrix
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AlwaysWhite.copy(alpha = 0.10f))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "가치 등급 산정 기준 (0~100점)",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlwaysWhite,
                                modifier = Modifier.weight(1f).padding(end = 6.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentTeal.copy(alpha = 0.22f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "내 점수 ${report.trustScore}점",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AccentTeal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Visual Tier Segments Bar (AAA, AA, A, B이하)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Box(modifier = Modifier.weight(15f).fillMaxHeight().background(Color(0xFFE0E0E0).copy(alpha = 0.4f))) // B이하 (0~69)
                            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(BrandNavyContainer))
                            Box(modifier = Modifier.weight(15f).fillMaxHeight().background(Color(0xFFFFB74D).copy(alpha = 0.75f))) // A (70~84)
                            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(BrandNavyContainer))
                            Box(modifier = Modifier.weight(10f).fillMaxHeight().background(Color(0xFF64B5F6))) // AA (85~94)
                            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(BrandNavyContainer))
                            Box(modifier = Modifier.weight(10f).fillMaxHeight().background(Color(0xFF4CAF50))) // AAA (95~100)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("B이하 (<70)", fontSize = 9.5.sp, color = AlwaysWhite.copy(alpha = 0.70f))
                            Text("A (70~84)", fontSize = 9.5.sp, color = AlwaysWhite.copy(alpha = 0.70f))
                            Text(
                                "AA (85~94)",
                                fontSize = 9.5.sp,
                                color = if (report.trustScore in 85.0f..94.9f) AccentTeal else AlwaysWhite.copy(alpha = 0.70f),
                                fontWeight = if (report.trustScore in 85.0f..94.9f) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                "AAA (95~100)",
                                fontSize = 9.5.sp,
                                color = if (report.trustScore >= 95.0f) AccentTeal else AlwaysWhite.copy(alpha = 0.70f),
                                fontWeight = if (report.trustScore >= 95.0f) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive Grade Guideline Button
                        Surface(
                            onClick = onOpenGradeGuidelineClick,
                            shape = RoundedCornerShape(10.dp),
                            color = AlwaysWhite.copy(alpha = 0.14f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AlwaysWhite.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_grade_guideline_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = AccentTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "등급 기준 상세 설명 & 상승 가이드라인 보기",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlwaysWhite
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "상세 보기",
                                    tint = AlwaysWhite.copy(alpha = 0.85f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = report.certificationNote,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AlwaysWhite.copy(alpha = 0.82f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Used Car Market Value Bonus & Residual Defense Banner (사용자 요청: 중고차 거래 객관적 입증 자료)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "중고차 거래 가치 산정 평가 결과",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 15.sp
                                )
                            )
                        }
                        StatusChip(
                            text = "객관적 실증",
                            backgroundColor = ChipVerifiedBg,
                            textColor = ChipVerifiedText
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceLight)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("종합 신뢰 점수", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${report.trustScore}점", fontWeight = FontWeight.Black, fontSize = 20.sp, color = PrimaryNavy)
                            Text("100점 만점", fontSize = 10.sp, color = TextTertiary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(SurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("잔존가치 방어율", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("+${report.residualValueDefenseRate}%p", fontWeight = FontWeight.Black, fontSize = 20.sp, color = AccentTeal)
                            Text("동급 매물 대비", fontSize = 10.sp, color = TextTertiary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(SurfaceBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("예상 시세 이점", fontSize = 11.sp, color = TextTertiary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("+${String.format("%,d", report.estimatedValueBonusWon / 10000)}만원", fontWeight = FontWeight.Black, fontSize = 20.sp, color = PrimaryBlue)
                            Text("추가 가치 인정", fontSize = 10.sp, color = TextTertiary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4-Pillar Evaluation Criteria Breakdown
                    Text(
                        text = "■ 4대 객관적 평가 기준 및 항목별 배점",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    report.evaluationCriteria.forEach { item ->
                        val ratio = if (item.weightScore > 0) item.earnedScore / item.weightScore.toFloat() else 0f
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.category,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${item.earnedScore} / ${item.weightScore}점 (${item.statusBadge})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { ratio.coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (ratio >= 0.95f) AccentTeal else PrimaryBlue,
                                trackColor = SurfaceBorderSubtle
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.detail,
                                fontSize = 10.5.sp,
                                color = TextTertiary
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Data Collection Scope & Gap Declaration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "1. 데이터 수집 범위 & 무결성",
                        subtitle = "기록 수집 기간: ${report.trackingPeriod}",
                        badgeText = "임의 추정값 없음",
                        badgeColor = ChipSensorBg,
                        badgeTextColor = ChipSensorText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sensor collected
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ChipVerifiedBg)
                                .border(1.dp, AccentGreen.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "차량 센서 직접 수집",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ChipVerifiedText
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${String.format("%.1f", report.sensorCollectionRatio)}%",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ChipVerifiedText,
                                    fontSize = 22.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Vehicle SDK 계기판 속도",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }

                        // Gap / unverified
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceLight)
                                .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "누락·미확인 구간",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${String.format("%.1f", report.missingOrGapRatio)}%",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary,
                                    fontSize = 22.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "터널 음영 구간 정직 표기",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextTertiary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "※ 지원되지 않거나 수집되지 않은 값은 알고리즘으로 임의 추정하여 채우지 않고, 누락 상태 그대로 보존하여 리포트의 법적·거래 신뢰도를 보장합니다.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextTertiary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Section 2: Maintenance Document Evidence Ratio
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "2. 정비 문서 뒷받침 내역",
                        subtitle = "영수증 및 정비 명세서 확인 비율",
                        badgeText = "증빙율 100%",
                        badgeColor = ChipVerifiedBg,
                        badgeTextColor = ChipVerifiedText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceLight)
                            .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "공식 정비 증빙 완료: 총 3건 중 3건",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "블루핸즈 및 공식 서비스센터 발행 명세서 원본 대조 완료",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Comparable vs Inconclusive/Pending Driving
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "3. 비교 가능한 결과 vs 판단 보류",
                        subtitle = "조건이 다른 주행을 분리하여 왜곡을 방지했습니다"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "비교 가능 주행",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${report.comparableTripsCount}회",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue,
                                    fontSize = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "동일 출퇴근 코스 검증",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "판단 보류 주행",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${report.pendingJudgmentTripsCount}회",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber,
                                    fontSize = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "폭우·우회 조건 상이",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "최근 코칭 성과: ${report.activeCoachingResult}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryNavy,
                            fontSize = 12.5.sp
                        )
                    )
                }
            }
        }

        // Export Button (Compact & well-spaced)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = onShareReportClick,
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("share_report_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = PrimaryBlueSubtle,
                        contentColor = PrimaryNavy
                    ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "리포트 공유 · 저장",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PrimaryNavy
                        ),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}
