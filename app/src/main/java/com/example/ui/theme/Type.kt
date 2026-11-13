package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)

val NotoSansKr = FontFamily(
    Font(R.font.noto_sans_kr, FontWeight.Normal)
)

// Do NOT hardcode colors in Typography so Material 3 LocalContentColor (e.g., onPrimary inside Buttons) works properly.
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        lineHeight = 60.sp,
        letterSpacing = (-1.0).sp,
        color = Color.Unspecified
    ),
    displayMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp,
        color = Color.Unspecified
    ),
    headlineMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
        color = Color.Unspecified
    ),
    titleLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.2).sp,
        color = Color.Unspecified
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp,
        color = Color.Unspecified
    ),
    titleSmall = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Color.Unspecified
    ),
    bodyLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    bodySmall = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    labelLarge = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        color = Color.Unspecified
    ),
    labelMedium = TextStyle(
        fontFamily = NotoSansKr,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = Color.Unspecified
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.3.sp,
        color = Color.Unspecified
    )
)
