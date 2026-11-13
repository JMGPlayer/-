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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun AppGuideDialog(
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
                .testTag("app_guide_dialog"),
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Car Value Coach 상세 이용 가이드",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "앱 소개 · 탭별 사용법 · 공정성 및 투명성 원칙",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_app_guide_button")
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
                    // Section 1: What is this app?
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandNavyContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            StatusChip(
                                text = "앱 핵심 소개",
                                backgroundColor = PrimaryBlue,
                                textColor = AlwaysWhite
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Car Value Coach는 어떤 앱인가요?",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.5.sp,
                                color = AlwaysWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "차량에 내장된 실제 센서 데이터(속도·연비/전비·누적 주행거리·타이어 공기압·배터리 상태)와 공식 정비소(블루핸즈 등) 영수증 증빙 이력을 하나로 연결해, 운전 습관을 개선하고 소모품을 적기에 관리하며, 훗날 차량을 매각할 때 '잘 관리된 차'임을 객관적 점수로 증명해주는 스마트 차량 가치 관리 앱입니다.",
                                fontSize = 12.5.sp,
                                color = AlwaysWhite.copy(alpha = 0.88f),
                                lineHeight = 18.5.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                GuideHighlightChip(text = "센서 직접 수집", modifier = Modifier.weight(1f))
                                GuideHighlightChip(text = "1일 1과제 코칭", modifier = Modifier.weight(1f))
                                GuideHighlightChip(text = "중고차 가치 방어", modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // Section 2: Step-by-Step How to Use Every Tab
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "화면별 구체적인 사용 방법 (4단계)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PrimaryNavy
                            )

                            StepGuideBlock(
                                icon = Icons.Default.DirectionsCar,
                                stepBadge = "STEP 1 · 내 차 (홈 화면)",
                                title = "내 차고 관리 & 실시간 주행 기록",
                                bullets = listOf(
                                    "상단 '내 차고'에서 보유 중인 차량을 전환하거나 [차량 추가 등록]으로 가족 차량을 등록할 수 있습니다.",
                                    "차량 대표 카드를 터치하면 제원 확인, 공동 이용 운전자 선택·직접 입력, 통상/가혹 주행 조건 변경이 가능합니다.",
                                    "출발 전 [실시간 주행 기록 시작하기]를 누르면 운전 중 방해 없이 계기판 속도와 거리만 안전하게 기록되며, 도착 후 종료 시 자동 분석됩니다.",
                                    "우측 상단 [센서 진단]으로 TPMS·배터리 CAN 상태를 점검하고, 톱니바퀴(⚙️)에서 다크 모드 및 알림 설정을 바꿀 수 있습니다."
                                )
                            )

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            StepGuideBlock(
                                icon = Icons.Default.Insights,
                                stepBadge = "STEP 2 · 주행 코치 탭",
                                title = "비교 가능한 주행만 공정하게 AI 코칭",
                                bullets = listOf(
                                    "여러 습관을 한꺼번에 지적하지 않고, 지금 가장 효과가 큰 AI 실천 과제 단 1개만 집중 추적합니다.",
                                    "동일한 출퇴근 코스는 '비교 가능'으로 묶어 실질 연비/전비 변화를 측정하며, 폭우·폭설·우회 주행은 '판단 보류'로 자동 분리해 억울한 감점을 막습니다.",
                                    "주행 기록 카드를 터치하면 시간축 속도·연비 추세 그래프를 확인하고, '운전자 상황 메모'를 직접 기록해 남길 수 있습니다."
                                )
                            )

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            StepGuideBlock(
                                icon = Icons.Default.Build,
                                stepBadge = "STEP 3 · 정비 관리 탭",
                                title = "공식 매뉴얼 기준 주기 & 영수증 증빙",
                                bullets = listOf(
                                    "도심 상습 정체·단거리 반복이 많다면 '가혹 조건'을 선택하세요. 현대차 공식 취급설명서 기준에 맞춰 교체 주기가 50% 단축 반영됩니다.",
                                    "[새 정비 내역 증빙 등록하기]에서 영수증 항목을 대조하고, 단순 '점검·보충'과 '교체'를 정확히 구분해 등록할 수 있습니다.",
                                    "등록된 정비 카드를 누르면 부품·공임 명세와 정비사 확인 내역이 담긴 상세 증빙 서류를 열람할 수 있습니다."
                                )
                            )

                            HorizontalDivider(color = SurfaceBorderSubtle)

                            StepGuideBlock(
                                icon = Icons.Default.Assessment,
                                stepBadge = "STEP 4 · 이력 리포트 탭",
                                title = "투명 가치 등급 확인 & 양도용 공유",
                                bullets = listOf(
                                    "센서 수집 무결성(40점) + 정비 증빙률(30점) + AI 코칭(20점) + 교체주기 준수(10점) 합산으로 AAA~B 등급이 자동 산출됩니다.",
                                    "[등급 기준 상세 설명 & 상승 가이드라인 보기]를 누르면 내 차량의 등급 구간과 점수 상승 방법을 바로 확인할 수 있습니다.",
                                    "중고차 판매나 양도 시 '번호판 마스킹' 및 '상세 경로 비식별화' 스위치를 켠 채 리포트를 안전하게 공유할 수 있습니다."
                                )
                            )
                        }
                    }

                    // Section 3: Transparency & Fairness Principles (FAQ)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlueSubtle),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "자주 묻는 질문 & 데이터 신뢰 원칙",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = PrimaryNavy
                                )
                            }

                            GuideFaqItem(
                                question = "Q. 급감속이 발생하면 무조건 운전 점수가 깎이나요?",
                                answer = "아니요. 전방 차량 끼어들기 등 안전 확보를 위한 방어 감속을 무작정 잘못된 습관으로 평가하지 않고, 도로 흐름 맥락을 분리해 안내합니다."
                            )
                            GuideFaqItem(
                                question = "Q. 터널에서 센서 기록이 잠깐 비면 오류가 나나요?",
                                answer = "임의 추정치로 빈칸을 채워 넣지 않고 '음영 누락 구간'으로 정직하게 기록합니다. 조작 없는 원본 보존이 오히려 중고차 리포트의 신뢰도를 높여줍니다."
                            )
                            GuideFaqItem(
                                question = "Q. 가족이 함께 타는 차는 어떻게 구분하나요?",
                                answer = "홈 화면 차량 카드를 눌러 '공동 이용 차량 운전자 설정'에서 주 운전자나 공동 운전자를 선택하거나 새 이름을 직접 입력하면 개인별 주행 이력이 분리됩니다."
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
                        .testTag("dismiss_app_guide_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "가이드 확인 완료 (닫기)",
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
private fun GuideHighlightChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AlwaysWhite.copy(alpha = 0.14f))
            .padding(horizontal = 6.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = AlwaysWhite,
            maxLines = 1
        )
    }
}

@Composable
private fun StepGuideBlock(
    icon: ImageVector,
    stepBadge: String,
    title: String,
    bullets: List<String>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlueSubtle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stepBadge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceLight)
                .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            bullets.forEach { line ->
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = line,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideFaqItem(
    question: String,
    answer: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfacePureWhite)
            .border(1.dp, PrimaryBlueBorder.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = question,
            fontWeight = FontWeight.Bold,
            fontSize = 12.5.sp,
            color = PrimaryNavy
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = answer,
            fontSize = 11.5.sp,
            color = TextSecondary,
            lineHeight = 16.5.sp
        )
    }
}
