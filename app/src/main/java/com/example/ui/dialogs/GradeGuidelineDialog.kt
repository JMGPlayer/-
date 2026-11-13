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
import com.example.data.model.TransparencyReport
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun GradeGuidelineDialog(
    report: TransparencyReport,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("grade_guideline_dialog"),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "투명 가치 등급 기준 & 가이드라인",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "100점 만점 객관적 실증 점수 산정 표준",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_grade_guideline_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = SurfaceBorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Current Score Summary Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandNavyContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "현재 내 차량 신뢰 등급",
                                        fontSize = 11.5.sp,
                                        color = AlwaysWhite.copy(alpha = 0.78f)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${report.transparencyGrade} (${report.trustScore}점)",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AlwaysWhite
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentTeal.copy(alpha = 0.25f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (report.trustScore >= 95f) "최상위 등급 유지 중"
                                        else "AAA까지 +${String.format("%.1f", (95.0f - report.trustScore).coerceAtLeast(0f))}점",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentTeal
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "센서 직접 수집률 ${String.format("%.1f", report.sensorCollectionRatio)}% · 공식 정비 증빙률 ${String.format("%.1f", report.documentVerifiedMaintenanceRatio)}% 기준으로 산출되었습니다.",
                                fontSize = 12.sp,
                                color = AlwaysWhite.copy(alpha = 0.85f),
                                lineHeight = 17.sp
                            )
                        }
                    }

                    // 1. Tier Thresholds & Residual Value Benefits
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "1. 등급별 점수 구간 및 중고차 가치 혜택",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = PrimaryNavy
                            )

                            GradeTierRow(
                                grade = "AAA 등급",
                                range = "95 ~ 100점",
                                badgeColor = Color(0xFF16A34A),
                                isCurrent = report.trustScore >= 95f,
                                description = "CAN 센서 95% 이상 무결성 + 공식 정비 100% 증빙 + 코칭·교체주기 최상위 준수. 동급 매물 대비 +4.5%p 이상 잔존가치 방어 기대."
                            )
                            GradeTierRow(
                                grade = "AA 등급",
                                range = "85 ~ 94.9점",
                                badgeColor = PrimaryBlue,
                                isCurrent = report.trustScore in 85f..94.99f,
                                description = "센서 직접 연동(80% 이상) 및 블루핸즈 공식 영수증 대조 완료 차량. 구매자 신뢰 확보로 평균 +3.8%p(약 +110만원) 시세 이점."
                            )
                            GradeTierRow(
                                grade = "A 등급",
                                range = "70 ~ 84.9점",
                                badgeColor = WarningAmber,
                                isCurrent = report.trustScore in 70f..84.99f,
                                description = "기본 주행·정비 기록이 양호하나 일부 수기 입력이나 미증빙 정비 내역이 포함된 상태. 영수증 추가 등록 시 AA 이상 승급 가능."
                            )
                            GradeTierRow(
                                grade = "B 등급 이하",
                                range = "70점 미만",
                                badgeColor = TextTertiary,
                                isCurrent = report.trustScore < 70f,
                                description = "센서 연동 이력이 부족하거나 공식 정비 명세서 대조가 미완료된 초기 상태."
                            )
                        }
                    }

                    // 2. 4-Pillar Score Weights Detail
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "2. 4대 평가 항목별 배점 기준 (총 100점)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = PrimaryNavy
                            )

                            report.evaluationCriteria.forEach { criteria ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceLight)
                                        .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = criteria.category,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            color = TextPrimary,
                                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                                        )
                                        StatusChip(
                                            text = "${criteria.earnedScore} / ${criteria.weightScore}점",
                                            backgroundColor = ChipSensorBg,
                                            textColor = ChipSensorText
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = criteria.detail,
                                        fontSize = 11.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    // 3. Actionable Guidelines Based on Current Report
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlueSubtle),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TipsAndUpdates,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "3. 등급 상승 & 신뢰 유지 실전 가이드라인",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy
                                )
                            }

                            GuidelineBullet(
                                index = "①",
                                title = "터널·음영 구간도 숨기지 않고 그대로 기록",
                                body = "현재 센서 직접 수집률은 ${String.format("%.1f", report.sensorCollectionRatio)}%입니다. 임의 추정 없이 실제 CAN 버스 데이터를 누적할수록 무결성 점수(40점 만점)가 높아집니다."
                            )
                            GuidelineBullet(
                                index = "②",
                                title = "정비 시 블루핸즈/공식 명세서 영수증 첨부",
                                body = "현재 증빙률은 ${String.format("%.1f", report.documentVerifiedMaintenanceRatio)}%입니다. 부품 품번·주행거리가 포함된 명세서를 등록하면 30점 만점을 안정적으로 유지할 수 있습니다."
                            )
                            GuidelineBullet(
                                index = "③",
                                title = "비교 가능 주행에서 AI 실천 과제 1개 꾸준히 완료",
                                body = "악천후·우회 등 비정상 주행은 자동 보류되므로, 일상 출퇴근 코칭 미션을 꾸준히 달성하면 코칭 점수(20점)가 최고 수준으로 반영됩니다."
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_grade_guideline_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "가이드라인 확인 완료",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = AlwaysWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun GradeTierRow(
    grade: String,
    range: String,
    badgeColor: Color,
    isCurrent: Boolean,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isCurrent) PrimaryBlueSubtle else SurfaceLight)
            .border(
                width = if (isCurrent) 1.5.dp else 1.dp,
                color = if (isCurrent) PrimaryBlue else SurfaceBorderSubtle,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = grade,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AlwaysWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = range,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            if (isCurrent) {
                StatusChip(
                    text = "현재 내 등급",
                    backgroundColor = PrimaryBlue,
                    textColor = AlwaysWhite
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            fontSize = 11.5.sp,
            color = TextSecondary,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun GuidelineBullet(
    index: String,
    title: String,
    body: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = index,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = PrimaryBlue
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = PrimaryNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = body,
                fontSize = 11.5.sp,
                color = TextSecondary,
                lineHeight = 16.5.sp
            )
        }
    }
}
