package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    badgeColor: Color = ChipSensorBg,
    badgeTextColor: Color = ChipSensorText
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextPrimary
                )
            )
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor)
                        .border(0.5.dp, badgeTextColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 9.dp, vertical = 3.5.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.5.sp
                )
            )
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(backgroundColor)
            .border(0.5.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(7.dp))
            .padding(horizontal = 8.dp, vertical = 3.5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = textColor
            )
        )
    }
}

@Composable
fun MiniSpeedChart(
    samples: List<Int>,
    lineColor: Color = PrimaryBlue,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
) {
    if (samples.isEmpty()) return

    val dotCenterColor = SurfacePureWhite
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceLight)
            .border(1.dp, SurfaceBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxVal = (samples.maxOrNull() ?: 100).toFloat().coerceAtLeast(60f)
            val minVal = 0f
            val widthStep = if (samples.size > 1) size.width / (samples.size - 1) else size.width

            val linePath = Path()
            val fillPath = Path()

            samples.forEachIndexed { index, value ->
                val x = index * widthStep
                val normalizedY = 1f - ((value - minVal) / (maxVal - minVal))
                val y = (normalizedY * (size.height - 12f)) + 6f

                if (index == 0) {
                    linePath.moveTo(x, y)
                    fillPath.moveTo(x, size.height)
                    fillPath.lineTo(x, y)
                } else {
                    linePath.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }

                if (index == samples.size - 1) {
                    fillPath.lineTo(x, size.height)
                    fillPath.close()
                }
            }

            // Draw subtle gradient under the curve
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.18f),
                        lineColor.copy(alpha = 0.01f)
                    )
                )
            )

            // Draw clean main stroke line
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw data milestone dots
            samples.forEachIndexed { index, value ->
                val x = index * widthStep
                val normalizedY = 1f - ((value - minVal) / (maxVal - minVal))
                val y = (normalizedY * (size.height - 12f)) + 6f
                drawCircle(
                    color = dotCenterColor,
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
                drawCircle(
                    color = lineColor,
                    radius = 1.8.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
fun TelemetryAxisChart(
    samples: List<Int>,
    unit: String = "km/h",
    durationMinutes: Int = 36,
    isTunnelGap: Boolean = false,
    lineColor: Color = PrimaryBlue,
    modifier: Modifier = Modifier
) {
    if (samples.isEmpty()) return

    val maxVal = (samples.maxOrNull() ?: 80).coerceAtLeast(60)
    val midVal = maxVal / 2

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Chart Header info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(lineColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "관측 데이터 추세선",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = PrimaryNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "최고 $maxVal $unit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = lineColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chart Canvas with explicit Y-axis and X-axis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            ) {
                // Y-Axis Labels
                Column(
                    modifier = Modifier
                        .width(42.dp)
                        .fillMaxHeight()
                        .padding(end = 6.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text("$maxVal", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.SemiBold)
                    Text("$midVal", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.SemiBold)
                    Text("0", fontSize = 10.sp, color = TextTertiary, fontWeight = FontWeight.SemiBold)
                }

                // Plot Area with Grid
                val gridColor = SurfaceBorder
                val gapHighlightColor = WarningAmber.copy(alpha = 0.22f)
                val chartDotCenterColor = SurfacePureWhite
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw horizontal grid lines
                        drawLine(color = gridColor, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 1.dp.toPx())
                        drawLine(color = gridColor, start = Offset(0f, size.height / 2), end = Offset(size.width, size.height / 2), strokeWidth = 1.dp.toPx())
                        drawLine(color = gridColor, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = 1.dp.toPx())

                        // Tunnel Gap Shaded Area if applicable
                        if (isTunnelGap && samples.size >= 5) {
                            val gapStart = size.width * 0.45f
                            val gapEnd = size.width * 0.65f
                            drawRect(
                                color = gapHighlightColor,
                                topLeft = Offset(gapStart, 0f),
                                size = androidx.compose.ui.geometry.Size(gapEnd - gapStart, size.height)
                            )
                        }

                        val widthStep = if (samples.size > 1) size.width / (samples.size - 1) else size.width
                        val linePath = Path()
                        val fillPath = Path()

                        samples.forEachIndexed { index, value ->
                            val x = index * widthStep
                            val normalizedY = 1f - (value.toFloat() / maxVal.toFloat())
                            val y = (normalizedY * (size.height - 8f)) + 4f

                            if (index == 0) {
                                linePath.moveTo(x, y)
                                fillPath.moveTo(x, size.height)
                                fillPath.lineTo(x, y)
                            } else {
                                linePath.lineTo(x, y)
                                fillPath.lineTo(x, y)
                            }

                            if (index == samples.size - 1) {
                                fillPath.lineTo(x, size.height)
                                fillPath.close()
                            }
                        }

                        // Draw Fill Gradient
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(lineColor.copy(alpha = 0.22f), lineColor.copy(alpha = 0.02f))
                            )
                        )

                        // Draw Main Line
                        drawPath(
                            path = linePath,
                            color = lineColor,
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Data Points & Milestone dots
                        samples.forEachIndexed { index, value ->
                            val x = index * widthStep
                            val normalizedY = 1f - (value.toFloat() / maxVal.toFloat())
                            val y = (normalizedY * (size.height - 8f)) + 4f

                            drawCircle(color = chartDotCenterColor, radius = 3.5.dp.toPx(), center = Offset(x, y))
                            drawCircle(color = lineColor, radius = 2.dp.toPx(), center = Offset(x, y))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // X-Axis Time Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0분 (출발)", fontSize = 10.sp, color = TextTertiary)
                Text("${durationMinutes / 3}분", fontSize = 10.sp, color = TextTertiary)
                Text("${(durationMinutes * 2) / 3}분", fontSize = 10.sp, color = TextTertiary)
                Text("${durationMinutes}분 (도착)", fontSize = 10.sp, color = TextTertiary)
            }
        }
    }
}

@Composable
fun ConsumableProgress(
    name: String,
    lastServicedKm: Int,
    nextDueKm: Int,
    currentMileageKm: Int,
    officialIntervalKm: Int,
    statusLabel: String,
    modifier: Modifier = Modifier
) {
    val usedKm = (currentMileageKm - lastServicedKm).coerceAtLeast(0)
    val remainingKm = (nextDueKm - currentMileageKm).coerceAtLeast(0)
    val progress = (usedKm.toFloat() / officialIntervalKm.toFloat()).coerceIn(0f, 1f)

    val isUrgent = remainingKm <= 2000
    val progressColor = when {
        isUrgent -> WarningAmber
        remainingKm == 0 -> AlertRed
        else -> AccentTeal
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUrgent) WarningAmber.copy(alpha = 0.4f) else SurfaceBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        ),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "이전 정비: ${String.format("%,d", lastServicedKm)} km · 다음 권장: ${String.format("%,d", nextDueKm)} km",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isUrgent) WarningAmberLight else ChipVerifiedBg)
                        .border(
                            0.5.dp,
                            if (isUrgent) WarningAmber.copy(alpha = 0.3f) else AccentGreen.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isUrgent) WarningAmber else AccentGreen,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Smooth progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(CircleShape),
                color = progressColor,
                trackColor = SurfaceSubtle
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "소모율 ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = TextTertiary,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = if (remainingKm > 0) "교체까지 잔여 ${String.format("%,d", remainingKm)} km" else "즉시 교체 필요",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = if (isUrgent) WarningAmber else PrimaryNavy
                    )
                )
            }
        }
    }
}
