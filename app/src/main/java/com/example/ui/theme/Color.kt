package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColorPalette(
    val isDark: Boolean,
    val primaryNavy: Color,
    val primaryNavyDark: Color,
    val primaryBlue: Color,
    val primaryBlueSubtle: Color,
    val primaryBlueBorder: Color,
    val accentTeal: Color,
    val accentTealLight: Color,
    val accentGreen: Color,
    val accentGreenLight: Color,
    val warningAmber: Color,
    val warningAmberLight: Color,
    val alertRed: Color,
    val alertRedLight: Color,
    val cleanWhite: Color,
    val surfacePureWhite: Color,
    val surfaceLight: Color,
    val surfaceSubtle: Color,
    val surfaceBorder: Color,
    val surfaceBorderSubtle: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val chipSensorBg: Color,
    val chipSensorText: Color,
    val chipVerifiedBg: Color,
    val chipVerifiedText: Color,
    val chipPendingBg: Color,
    val chipPendingText: Color,
    val chipGapBg: Color,
    val chipGapText: Color,
    val brandNavyContainer: Color,
    val brandButtonContainer: Color
)

val LightAppPalette = AppColorPalette(
    isDark = false,
    primaryNavy = Color(0xFF0A192F),
    primaryNavyDark = Color(0xFF06101E),
    primaryBlue = Color(0xFF1E60F2),
    primaryBlueSubtle = Color(0xFFEEF4FF),
    primaryBlueBorder = Color(0xFFCCE0FF),
    accentTeal = Color(0xFF009688),
    accentTealLight = Color(0xFFE6F4F1),
    accentGreen = Color(0xFF059669),
    accentGreenLight = Color(0xFFECFDF5),
    warningAmber = Color(0xFFD97706),
    warningAmberLight = Color(0xFFFFFBEB),
    alertRed = Color(0xFFDC2626),
    alertRedLight = Color(0xFFFEF2F2),
    cleanWhite = Color(0xFFFFFFFF),
    surfacePureWhite = Color(0xFFFFFFFF),
    surfaceLight = Color(0xFFF8FAFD),
    surfaceSubtle = Color(0xFFF1F5F9),
    surfaceBorder = Color(0xFFE2E8F0),
    surfaceBorderSubtle = Color(0xFFEEF2F6),
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textTertiary = Color(0xFF94A3B8),
    textMuted = Color(0xFFCBD5E1),
    chipSensorBg = Color(0xFFF0F6FF),
    chipSensorText = Color(0xFF1D4ED8),
    chipVerifiedBg = Color(0xFFECFDF5),
    chipVerifiedText = Color(0xFF047857),
    chipPendingBg = Color(0xFFFFFBEB),
    chipPendingText = Color(0xFFB45309),
    chipGapBg = Color(0xFFF8FAFC),
    chipGapText = Color(0xFF64748B),
    brandNavyContainer = Color(0xFF0A192F),
    brandButtonContainer = Color(0xFF0A192F)
)

val DarkAppPalette = AppColorPalette(
    isDark = true,
    primaryNavy = Color(0xFFE2E8F0),
    primaryNavyDark = Color(0xFF090F1A),
    primaryBlue = Color(0xFF4D8BFF),
    primaryBlueSubtle = Color(0xFF162544),
    primaryBlueBorder = Color(0xFF28467D),
    accentTeal = Color(0xFF2DD4BF),
    accentTealLight = Color(0xFF113536),
    accentGreen = Color(0xFF34D399),
    accentGreenLight = Color(0xFF0D3327),
    warningAmber = Color(0xFFFBBF24),
    warningAmberLight = Color(0xFF3B2A10),
    alertRed = Color(0xFFF87171),
    alertRedLight = Color(0xFF3D1719),
    cleanWhite = Color(0xFF0E1624),
    surfacePureWhite = Color(0xFF131D2E),
    surfaceLight = Color(0xFF172336),
    surfaceSubtle = Color(0xFF1E2D45),
    surfaceBorder = Color(0xFF2B3C59),
    surfaceBorderSubtle = Color(0xFF22314A),
    textPrimary = Color(0xFFF1F5F9),
    textSecondary = Color(0xFFB3C0D4),
    textTertiary = Color(0xFF7D8FA8),
    textMuted = Color(0xFF475569),
    chipSensorBg = Color(0xFF172B4D),
    chipSensorText = Color(0xFF7AB2FF),
    chipVerifiedBg = Color(0xFF0D3327),
    chipVerifiedText = Color(0xFF34D399),
    chipPendingBg = Color(0xFF3B2A10),
    chipPendingText = Color(0xFFFBBF24),
    chipGapBg = Color(0xFF1B273A),
    chipGapText = Color(0xFF94A3B8),
    brandNavyContainer = Color(0xFF132642),
    brandButtonContainer = Color(0xFF1E60F2)
)

val LocalAppColors = staticCompositionLocalOf { LightAppPalette }

val AlwaysWhite = Color(0xFFFFFFFF)
val BrandDarkNavy = Color(0xFF0A192F)

val PrimaryNavy: Color @Composable get() = LocalAppColors.current.primaryNavy
val PrimaryNavyDark: Color @Composable get() = LocalAppColors.current.primaryNavyDark
val PrimaryBlue: Color @Composable get() = LocalAppColors.current.primaryBlue
val PrimaryBlueSubtle: Color @Composable get() = LocalAppColors.current.primaryBlueSubtle
val PrimaryBlueBorder: Color @Composable get() = LocalAppColors.current.primaryBlueBorder

val AccentTeal: Color @Composable get() = LocalAppColors.current.accentTeal
val AccentTealLight: Color @Composable get() = LocalAppColors.current.accentTealLight
val AccentGreen: Color @Composable get() = LocalAppColors.current.accentGreen
val AccentGreenLight: Color @Composable get() = LocalAppColors.current.accentGreenLight
val WarningAmber: Color @Composable get() = LocalAppColors.current.warningAmber
val WarningAmberLight: Color @Composable get() = LocalAppColors.current.warningAmberLight
val AlertRed: Color @Composable get() = LocalAppColors.current.alertRed
val AlertRedLight: Color @Composable get() = LocalAppColors.current.alertRedLight

val CleanWhite: Color @Composable get() = LocalAppColors.current.cleanWhite
val SurfacePureWhite: Color @Composable get() = LocalAppColors.current.surfacePureWhite
val SurfaceLight: Color @Composable get() = LocalAppColors.current.surfaceLight
val SurfaceSubtle: Color @Composable get() = LocalAppColors.current.surfaceSubtle
val SurfaceBorder: Color @Composable get() = LocalAppColors.current.surfaceBorder
val SurfaceBorderSubtle: Color @Composable get() = LocalAppColors.current.surfaceBorderSubtle

val TextPrimary: Color @Composable get() = LocalAppColors.current.textPrimary
val TextSecondary: Color @Composable get() = LocalAppColors.current.textSecondary
val TextTertiary: Color @Composable get() = LocalAppColors.current.textTertiary
val TextMuted: Color @Composable get() = LocalAppColors.current.textMuted

val ChipSensorBg: Color @Composable get() = LocalAppColors.current.chipSensorBg
val ChipSensorText: Color @Composable get() = LocalAppColors.current.chipSensorText
val ChipVerifiedBg: Color @Composable get() = LocalAppColors.current.chipVerifiedBg
val ChipVerifiedText: Color @Composable get() = LocalAppColors.current.chipVerifiedText
val ChipPendingBg: Color @Composable get() = LocalAppColors.current.chipPendingBg
val ChipPendingText: Color @Composable get() = LocalAppColors.current.chipPendingText
val ChipGapBg: Color @Composable get() = LocalAppColors.current.chipGapBg
val ChipGapText: Color @Composable get() = LocalAppColors.current.chipGapText

val BrandNavyContainer: Color @Composable get() = LocalAppColors.current.brandNavyContainer
val BrandButtonContainer: Color @Composable get() = LocalAppColors.current.brandButtonContainer
