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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppSettings
import com.example.ui.theme.*

@Composable
fun SettingsDialog(
    settings: AppSettings,
    loggedInUserName: String,
    loggedInUserId: String,
    onUpdateSettings: (AppSettings) -> Unit,
    onLogoutClick: () -> Unit,
    onConnectionCheckClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("settings_dialog"),
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "환경설정",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "화면 테마, 주행 연동 및 알림 기본값 관리",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_button")
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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "차량 연결 확인",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Pleos 데이터 수신 상태와 주행 기록을 확인합니다.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = onConnectionCheckClick,
                                modifier = Modifier.testTag("open_pleos_diagnostics_button")
                            ) {
                                Text("연결 확인 열기")
                            }
                        }
                    }

                    // 1. Display & Theme (다크 모드)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "화면 및 테마 설정",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CleanWhite)
                                    .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(12.dp))
                                    .clickable {
                                        onUpdateSettings(settings.copy(isDarkMode = !settings.isDarkMode))
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("settings_dark_mode_row"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlueSubtle),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (settings.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                            contentDescription = null,
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "다크 모드 (야간 주행 테마)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (settings.isDarkMode) "어두운 다크 네이비 테마 적용 중" else "밝은 클린 라이트 테마 적용 중",
                                            fontSize = 11.5.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                Switch(
                                    checked = settings.isDarkMode,
                                    onCheckedChange = {
                                        onUpdateSettings(settings.copy(isDarkMode = it))
                                    },
                                    modifier = Modifier.testTag("settings_dark_mode_switch")
                                )
                            }
                        }
                    }

                    // 2. Driving & AI Coaching Preferences
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "주행 기록 & AI 코칭 설정",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryNavy
                            )

                            SettingsToggleItem(
                                title = "시동 연동 자동 주행 감지",
                                subtitle = "현대 Vehicle SDK 시동 신호 시 자동으로 세션 대기",
                                checked = settings.autoDetectDrive,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(autoDetectDrive = it))
                                },
                                testTag = "settings_auto_drive_switch"
                            )

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            SettingsToggleItem(
                                title = "도착 후 AI 코칭 요약 알림",
                                subtitle = "운전 중 알림은 차단하고 정차·도착 후에만 결과 표시",
                                checked = settings.coachingAlertsEnabled,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(coachingAlertsEnabled = it))
                                },
                                testTag = "settings_coaching_alert_switch"
                            )

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            SettingsToggleItem(
                                title = "주행 그래프 축 눈금 상세 표시",
                                subtitle = "속도 및 연비/전비 그래프 X·Y축 기준값 항상 표기",
                                checked = settings.showDetailAxisLabels,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(showDetailAxisLabels = it))
                                },
                                testTag = "settings_axis_label_switch"
                            )
                        }
                    }

                    // 3. Maintenance & Report Defaults
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "정비 알림 & 리포트 보안 설정",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryNavy
                            )

                            Column {
                                Text(
                                    text = "소모품 교체 사전 알림 기준 거리",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(1000, 2000, 3000).forEach { km ->
                                        val selected = settings.maintenanceAlertThresholdKm == km
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (selected) BrandButtonContainer else CleanWhite)
                                                .border(
                                                    1.dp,
                                                    if (selected) BrandButtonContainer else SurfaceBorder,
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clickable {
                                                    onUpdateSettings(settings.copy(maintenanceAlertThresholdKm = km))
                                                }
                                                .padding(vertical = 9.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "잔여 ${String.format("%,d", km)}km",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) AlwaysWhite else TextPrimary
                                            )
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            SettingsToggleItem(
                                title = "리포트 번호판 자동 마스킹 기본 적용",
                                subtitle = "중고차 양도 리포트 열람 시 번호판 뒷자리 기본 가림",
                                checked = settings.defaultPrivacyMasking,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(defaultPrivacyMasking = it))
                                },
                                testTag = "settings_privacy_mask_switch"
                            )
                        }
                    }

                    // 4. Connected Hyundai Account Info & Logout
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "현대자동차 연동 계정",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlueSubtle),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "$loggedInUserName 님 (블루멤버스 연동)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = loggedInUserId,
                                            fontSize = 11.5.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = onLogoutClick,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed.copy(alpha = 0.5f)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("settings_logout_button")
                                ) {
                                    Text(
                                        text = "로그아웃",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlertRed
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = SurfaceBorderSubtle)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "앱 버전 v1.5 · Galaxy S21 / AAOS Vehicle SDK 호환",
                                fontSize = 11.sp,
                                color = TextTertiary
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
                        .testTag("dismiss_settings_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "설정 저장 및 닫기",
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
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}
