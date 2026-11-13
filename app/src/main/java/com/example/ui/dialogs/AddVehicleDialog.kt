package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
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
import com.example.data.model.PowerTrainType
import com.example.ui.theme.*

@Composable
fun AddVehicleDialog(
    onConfirm: (nickname: String, modelName: String, plateNumber: String, powerTrain: PowerTrainType, driver: String) -> Unit,
    onDismiss: () -> Unit
) {
    var nickname by remember { mutableStateOf("패밀리 SUV") }
    var modelName by remember { mutableStateOf("디 올 뉴 싼타페 하이브리드") }
    var plateNumber by remember { mutableStateOf("290로 4819") }
    var driver by remember { mutableStateOf("이수진 (배우자)") }
    var selectedPowerTrain by remember { mutableStateOf(PowerTrainType.HYBRID) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(22.dp))
                .testTag("add_vehicle_dialog"),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
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
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "차고 새 차량 추가 등록",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "가족 차량 또는 추가 보유 차량을 등록합니다",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = PrimaryNavy)
                    }
                }

                Divider(color = SurfaceBorderSubtle)

                // Nickname
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("차량 별칭") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Model Name
                OutlinedTextField(
                    value = modelName,
                    onValueChange = { modelName = it },
                    label = { Text("차종 및 모델명") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Plate Number
                OutlinedTextField(
                    value = plateNumber,
                    onValueChange = { plateNumber = it },
                    label = { Text("차량 번호판 (예: 290로 4819)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Driver
                OutlinedTextField(
                    value = driver,
                    onValueChange = { driver = it },
                    label = { Text("주 운전자 성명") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // PowerTrain selector
                Text("동력계 유형 선택", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PowerTrainType.values().forEach { pt ->
                        val isSelected = pt == selectedPowerTrain
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedPowerTrain = pt },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) BrandNavyContainer else SurfaceLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) BrandNavyContainer else SurfaceBorderSubtle
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = when (pt) {
                                        PowerTrainType.HYBRID -> "하이브리드"
                                        PowerTrainType.EV -> "전기차"
                                        PowerTrainType.GASOLINE -> "가솔린"
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AlwaysWhite else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (modelName.isNotBlank() && plateNumber.isNotBlank()) {
                            onConfirm(nickname, modelName, plateNumber, selectedPowerTrain, driver)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_add_vehicle_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandButtonContainer,
                        contentColor = AlwaysWhite
                    )
                ) {
                    Text(
                        text = "차고에 차량 등록 완료",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = AlwaysWhite
                    )
                }
            }
        }
    }
}
