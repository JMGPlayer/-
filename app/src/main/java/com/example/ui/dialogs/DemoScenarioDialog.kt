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
import com.example.data.repository.DemoScenarioType
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun DemoScenarioDialog(
    currentScenario: DemoScenarioType,
    onSelectScenario: (DemoScenarioType) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("demo_scenario_dialog"),
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
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "데모 시나리오 & 데이터 프리셋",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "테스터 및 심사위원을 위한 데이터 프리셋 제어기",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_scenario_button")
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "원하는 상태를 선택하면 즉시 해당 시나리오로 전환되어 빈 상태, 온보딩, 코칭 성과 및 점검 알림을 테스트할 수 있습니다.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }

                    DemoScenarioType.values().forEach { scenario ->
                        val isSelected = scenario == currentScenario
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onSelectScenario(scenario) }
                                .testTag("scenario_card_${scenario.name.lowercase()}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) PrimaryBlueSubtle else CleanWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) PrimaryBlue else SurfaceBorder
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = scenario.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.5.sp,
                                            color = if (isSelected) PrimaryNavy else TextPrimary
                                        )
                                    )
                                    if (isSelected) {
                                        StatusChip(
                                            text = "현재 적용 중",
                                            backgroundColor = BrandNavyContainer,
                                            textColor = AlwaysWhite
                                        )
                                    } else {
                                        StatusChip(
                                            text = "선택하여 적용",
                                            backgroundColor = SurfaceLight,
                                            textColor = TextSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = scenario.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.5.sp,
                                        lineHeight = 17.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val testFocus = when (scenario) {
                                    DemoScenarioType.STANDARD_VERIFIED -> "🎯 검증 포인트: 실천 과제 달성률(+1.3km/L), 블루핸즈 영수증 100% 증빙, AAA 투명 가치 리포트"
                                    DemoScenarioType.IONIQ5_EV -> "🎯 검증 포인트: 전기차 전비(km/kWh), 고전압 배터리 SOC 74%, 회생제동 원페달 코칭, 감속기 오일 점검표"
                                    DemoScenarioType.NEW_CAR_EMPTY -> "🎯 검증 포인트: 주행 기록 0건 빈 화면, 정비 영수증 0건 빈 화면, 3단계 초보자 온보딩 플로우"
                                    DemoScenarioType.SEVERE_CONDITION_ALERT -> "🎯 검증 포인트: 가혹 조건 5,000km 점검 임박 알림, 악천후 폭설 안전주행 판단보류 분리"
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CleanWhite else SurfaceLight)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = testFocus,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = if (isSelected) PrimaryNavy else TextTertiary,
                                            lineHeight = 15.sp
                                        )
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
                        .testTag("dismiss_scenario_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "완료",
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
