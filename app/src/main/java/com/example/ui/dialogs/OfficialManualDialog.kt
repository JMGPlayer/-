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
import com.example.data.model.VehicleProfile
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun OfficialManualDialog(
    vehicle: VehicleProfile,
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
                .testTag("official_manual_dialog"),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "현대자동차 공식 매뉴얼",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "${vehicle.modelName} (${vehicle.modelYear}년형)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_manual_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = SurfaceBorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Manual Citation Notice
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ChipVerifiedBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ChipVerifiedText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "현대차 공식 사용자 취급설명서 정기점검표(제9장 유지관리) 원문 기준 발췌",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ChipVerifiedText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }

                    // Section 1: Usage Condition Definition
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "1. 주행 환경별 관리 주기 기준",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Normal Condition
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceLight)
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StatusChip(
                                        text = "통상 조건",
                                        backgroundColor = BrandNavyContainer,
                                        textColor = AlwaysWhite
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "일반적인 고속 및 중장거리 정속 주행 환경",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryNavy
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• 엔진오일: 매 10,000km 또는 12개월마다 교체\n• 에어컨 필터: 매 15,000km 또는 12개월마다 교체\n• 브레이크 액: 매 50,000km 또는 24개월마다 점검 및 교체",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Severe Condition
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WarningAmberLight)
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StatusChip(
                                        text = "가혹 조건",
                                        backgroundColor = WarningAmber,
                                        textColor = AlwaysWhite
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "도심 상습 정체, 8km 이내 단거리 반복, 험로 주행",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = WarningAmber
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• 엔진오일: 매 5,000km 또는 6개월마다 조기 교체 권장 (주기 50% 단축)\n• 브레이크 패드 및 오일: 매 25,000km마다 빈번 점검\n• 변속기 오일: 매 50,000km마다 점검 및 교체",
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Section 2: Powertrain Specific Guide
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "2. ${vehicle.fuelType} 특화 점검 지침",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (vehicle.powerTrainType == com.example.data.model.PowerTrainType.EV) {
                                Text(
                                    text = "• 고전압 배터리 냉각수(절연형): 매 60,000km 또는 36개월 점검\n• 감속기 오일: 매 60,000km마다 점검\n• 회생 제동 패드 마모도: 12개월 정기 검사",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            } else {
                                Text(
                                    text = "• 하이브리드 고전압 모터 냉각수: 매 200,000km 또는 10년 최초 교체\n• 스마트스트림 터보/가솔린 엔진오일(0W-16/0W-20 규격 준수 필수)\n• 구동모터 및 인버터 하이브리드 제어 시스템 전산 스캔",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    // Section 3: Official Bluehands Network
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "현대자동차 직영 서비스센터 및 블루핸즈 안내",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryNavy
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "전국 1,300여 개 블루핸즈에서 순정 부품 사용 시 1년/20,000km 품질 보증이 제공되며, 정비 명세서는 Car Value Coach 리포트의 100% 공식 증빙으로 자동 인정됩니다.",
                                fontSize = 11.5.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Close Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_manual_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "매뉴얼 확인 완료",
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
