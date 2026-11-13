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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.Color
import com.example.data.model.MaintenanceActionType
import com.example.data.model.VerificationLevel
import com.example.ui.theme.*

@Composable
fun MaintenanceRegisterDialog(
    currentMileageKm: Int,
    onConfirm: (
        workshop: String,
        mileage: Int,
        items: List<String>,
        actionType: MaintenanceActionType,
        cost: Int,
        isVerified: Boolean,
        verificationLevel: VerificationLevel,
        documentTitle: String,
        memo: String
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var workshopName by remember { mutableStateOf("현대 블루핸즈 강남점") }
    var mileageText by remember { mutableStateOf(currentMileageKm.toString()) }
    var costText by remember { mutableStateOf("95000") }
    var selectedActionType by remember { mutableStateOf(MaintenanceActionType.REPLACEMENT) }
    var selectedVerificationLevel by remember { mutableStateOf(VerificationLevel.DATA_MATCHED) }
    var selectedItemEngineOil by remember { mutableStateOf(true) }
    var selectedItemCabinFilter by remember { mutableStateOf(true) }
    var selectedItemBrakeCheck by remember { mutableStateOf(false) }
    var selectedItemTire by remember { mutableStateOf(false) }
    var isVerifiedDocument by remember { mutableStateOf(true) }
    var documentTitle by remember { mutableStateOf("공식 정비명세서 (블루핸즈 발행)") }
    var userConfirmedTerms by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .testTag("maintenance_register_dialog"),
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
                    Column {
                        Text(
                            text = "정비 증빙 등록 & 항목 대조",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "명세서 내용 대조 후 공식 관리 이력에 반영",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_register_dialog_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = PrimaryNavy)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = SurfaceBorderSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // OCR Extraction Banner Notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlueSubtle)
                            .border(1.dp, PrimaryBlueBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DocumentScanner,
                                    contentDescription = null,
                                    tint = AlwaysWhite,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "영수증 이미지에서 정비소명, 주행거리, 정비 항목을 자동 추출했습니다. 원본과 대조 후 저장해주세요.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = PrimaryNavy,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }

                    // Work Action Type Selector (점검 / 보충 / 교체 - 기획서 요구사항: 단순 점검·보충을 교체로 처리하지 않음)
                    Column {
                        Text(
                            text = "작업 구분 (공식 취급설명서 기준)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MaintenanceActionType.values().forEach { action ->
                                val isSelected = selectedActionType == action
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) BrandButtonContainer else SurfaceLight)
                                        .border(
                                            1.dp,
                                            if (isSelected) BrandButtonContainer else SurfaceBorderSubtle,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedActionType = action }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = action.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) AlwaysWhite else TextSecondary,
                                            fontSize = 12.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Workshop Name Field
                    OutlinedTextField(
                        value = workshopName,
                        onValueChange = { workshopName = it },
                        label = { Text("정비 사업소 명칭") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("workshop_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Mileage & Cost Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = mileageText,
                            onValueChange = { mileageText = it },
                            label = { Text("정비 주행거리 (km)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("mileage_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = costText,
                            onValueChange = { costText = it },
                            label = { Text("결제 금액 (원)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("cost_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Work Items Checklist
                    Column {
                        Text(
                            text = "확인된 작업 항목 (복수 선택 가능)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItemEngineOil = !selectedItemEngineOil }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Checkbox(checked = selectedItemEngineOil, onCheckedChange = { selectedItemEngineOil = it })
                                Text(text = "엔진오일 / 감속기 오일 점검 및 교체", fontSize = 13.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItemCabinFilter = !selectedItemCabinFilter }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Checkbox(checked = selectedItemCabinFilter, onCheckedChange = { selectedItemCabinFilter = it })
                                Text(text = "에어컨 / 히터 필터 교체 (순정 항균)", fontSize = 13.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItemBrakeCheck = !selectedItemBrakeCheck }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Checkbox(checked = selectedItemBrakeCheck, onCheckedChange = { selectedItemBrakeCheck = it })
                                Text(text = "브레이크 패드 및 오일 점검", fontSize = 13.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedItemTire = !selectedItemTire }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Checkbox(checked = selectedItemTire, onCheckedChange = { selectedItemTire = it })
                                Text(text = "타이어 위치 교환 및 4륜 공기압", fontSize = 13.sp)
                            }
                        }
                    }

                    // Verification Level 4-Stage Selection (기획서 4페이지)
                    Column {
                        Text(
                            text = "증빙 확인 수준 (4단계 표준)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            VerificationLevel.values().forEach { level ->
                                val isSelected = selectedVerificationLevel == level
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ChipSensorBg else SurfaceLight)
                                        .border(
                                            1.dp,
                                            if (isSelected) PrimaryBlue else SurfaceBorderSubtle,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedVerificationLevel = level }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = level.label,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) PrimaryBlue else TextTertiary
                                    )
                                }
                            }
                        }
                    }

                    // Confirmation Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { userConfirmedTerms = !userConfirmedTerms }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(checked = userConfirmedTerms, onCheckedChange = { userConfirmedTerms = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "추출된 내역을 확인했으며, 공식 관리 이력에 반영합니다.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontSize = 12.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Text("취소", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            val items = mutableListOf<String>()
                            if (selectedItemEngineOil) items.add("엔진오일 / 감속기 오일 점검 및 교체")
                            if (selectedItemCabinFilter) items.add("에어컨 / 히터 필터 교체")
                            if (selectedItemBrakeCheck) items.add("브레이크 패드 및 오일 점검")
                            if (selectedItemTire) items.add("타이어 위치 교환 및 공기압")
                            if (items.isEmpty()) items.add("일반 기본 점검")

                            val mileage = mileageText.toIntOrNull() ?: currentMileageKm
                            val cost = costText.toIntOrNull() ?: 0

                            onConfirm(
                                workshopName,
                                mileage,
                                items,
                                selectedActionType,
                                cost,
                                isVerifiedDocument,
                                selectedVerificationLevel,
                                documentTitle,
                                "사용자 항목 대조 및 증빙 서류 확인 완료"
                            )
                        },
                        enabled = userConfirmedTerms && workshopName.isNotBlank(),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(48.dp)
                            .testTag("submit_maintenance_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = AlwaysWhite,
                            disabledContainerColor = SurfaceBorder,
                            disabledContentColor = TextTertiary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "확인 및 이력 등록",
                            color = if (userConfirmedTerms && workshopName.isNotBlank()) AlwaysWhite else TextTertiary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp
                        )
                    }
                }
            }
        }
    }
}
