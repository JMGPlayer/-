package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = LightAppPalette.brandButtonContainer,
    onPrimary = AlwaysWhite,
    primaryContainer = LightAppPalette.chipSensorBg,
    onPrimaryContainer = LightAppPalette.primaryNavy,
    secondary = LightAppPalette.primaryBlue,
    onSecondary = AlwaysWhite,
    secondaryContainer = LightAppPalette.surfaceLight,
    onSecondaryContainer = LightAppPalette.primaryNavy,
    tertiary = LightAppPalette.accentTeal,
    onTertiary = AlwaysWhite,
    background = LightAppPalette.cleanWhite,
    onBackground = LightAppPalette.textPrimary,
    surface = LightAppPalette.cleanWhite,
    onSurface = LightAppPalette.textPrimary,
    surfaceVariant = LightAppPalette.surfaceLight,
    onSurfaceVariant = LightAppPalette.textSecondary,
    outline = LightAppPalette.surfaceBorder,
    outlineVariant = LightAppPalette.surfaceBorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkAppPalette.brandButtonContainer,
    onPrimary = AlwaysWhite,
    primaryContainer = DarkAppPalette.chipSensorBg,
    onPrimaryContainer = DarkAppPalette.primaryNavy,
    secondary = DarkAppPalette.primaryBlue,
    onSecondary = AlwaysWhite,
    secondaryContainer = DarkAppPalette.surfaceLight,
    onSecondaryContainer = DarkAppPalette.primaryNavy,
    tertiary = DarkAppPalette.accentTeal,
    onTertiary = AlwaysWhite,
    background = DarkAppPalette.cleanWhite,
    onBackground = DarkAppPalette.textPrimary,
    surface = DarkAppPalette.cleanWhite,
    onSurface = DarkAppPalette.textPrimary,
    surfaceVariant = DarkAppPalette.surfaceLight,
    onSurfaceVariant = DarkAppPalette.textSecondary,
    outline = DarkAppPalette.surfaceBorder,
    outlineVariant = DarkAppPalette.surfaceBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) DarkAppPalette else LightAppPalette
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = palette.cleanWhite.toArgb()
                it.navigationBarColor = palette.cleanWhite.toArgb()
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    CompositionLocalProvider(LocalAppColors provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
