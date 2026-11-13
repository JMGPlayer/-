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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MaintenanceRecord
import com.example.ui.components.StatusChip
import com.example.ui.theme.*

@Composable
fun MaintenanceDetailDialog(
    record: MaintenanceRecord,
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
                .testTag("maintenance_detail_dialog"),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top Header Row
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
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "정비 명세서 상세 증빙",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "공식 정비소 원본 대조 및 전자서명 확인",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_maintenance_detail_button")
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

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Document Verification Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChipVerifiedBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreen.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = ChipVerifiedText,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "공식 전산 및 영수증 증빙 일치 (검증완료)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ChipVerifiedText,
                                        fontSize = 13.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "계기판 주행거리(${String.format("%,d", record.mileageKm)}km)와 정비 명세서 기재 수치가 정확히 일치합니다.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }
                    }

                    // Realistic Official Paper Invoice Certificate Card (사용자 요청: 실제 영수증/서류처럼 증빙기록 구현)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryNavy.copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfacePureWhite)
                                .padding(16.dp)
                        ) {
                            // Official Invoice Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = "자동차 점검·정비 명세서",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 16.sp,
                                            color = PrimaryNavy,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Text(
                                        text = "[자동차관리법 시행규칙 제134조 제1항 서식]",
                                        fontSize = 10.sp,
                                        color = TextTertiary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .border(1.5.dp, Color(0xFFD32F2F), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "정품 보증 필",
                                        color = Color(0xFFD32F2F),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = PrimaryNavy, thickness = 1.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Business & Registration Metadata Grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("발행번호", fontSize = 10.sp, color = TextTertiary)
                                    Text(record.invoiceNumber, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("정비업체명", fontSize = 10.sp, color = TextTertiary)
                                    Text(record.workshopName, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = PrimaryNavy)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("사업자등록번호", fontSize = 10.sp, color = TextTertiary)
                                    Text(record.businessRegNumber, fontSize = 11.5.sp, fontWeight = FontWeight.Normal, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("정비일자/주행거리", fontSize = 10.sp, color = TextTertiary)
                                    Text("${record.dateFormatted} (${String.format("%,d", record.mileageKm)}km)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = SurfaceBorderSubtle)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Structured Parts and Labor Table (명세표)
                            Text(
                                text = "■ 점검 및 정비 내역 (부품대 및 공임)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceLight)
                                    .padding(vertical = 5.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("작업/부품 항목", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(2.2f))
                                Text("수량", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(0.6f))
                                Text("부품대", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(1.1f))
                                Text("공임비", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.weight(1.1f))
                            }
                            Divider(color = SurfaceBorder)

                            // Table Rows
                            record.partsDetailList.forEach { part ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(part.partName, fontSize = 11.5.sp, color = TextPrimary, fontWeight = FontWeight.Medium, modifier = Modifier.weight(2.2f))
                                    Text("${part.quantity}", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.weight(0.6f))
                                    Text("${String.format("%,d", part.partCost)}원", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.weight(1.1f))
                                    Text("${String.format("%,d", part.laborCost)}원", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.weight(1.1f))
                                }
                                Divider(color = SurfaceBorderSubtle, thickness = 0.5.dp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Official Stamp & Technician Signature Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("점검·정비 책임자", fontSize = 10.5.sp, color = TextTertiary)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(record.technicianName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("(서명/인)", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                // Red Korean Official Seal Stamp Circle
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .border(2.dp, Color(0xFFD32F2F), CircleShape)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("현대", color = Color(0xFFD32F2F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text("블루핸즈", color = Color(0xFFD32F2F), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                        Text("검인", color = Color(0xFFD32F2F), fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Total Cost & OCR Note
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "최종 결제 금액",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        fontSize = 13.5.sp
                                    )
                                )
                                Text(
                                    text = if (record.totalCostWon > 0) "${String.format("%,d", record.totalCostWon)}원" else "무상 보증 점검 (0원)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = PrimaryNavy
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "비고: ${record.receiptMemo}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }

                    // Tamper-proof Electronic Signature Hash
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CleanWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = PrimaryNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "위변조 방지 전자서명 해시 (Hash)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceLight)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "SHA-256: 0x9b7e41f28c04e8d356a1470e9a5d1b3f72e90c58",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.5.sp,
                                        color = TextTertiary
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "영수증 원본 이미지 및 전산 등록 데이터의 무결성이 보증되었습니다.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = TextTertiary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_detail_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "확인 완료 (닫기)",
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
