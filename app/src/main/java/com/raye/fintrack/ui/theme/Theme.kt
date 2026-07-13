package com.raye.fintrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FinPrimaryDark,
    onPrimary = FinOnPrimaryDark,
    primaryContainer = FinPrimaryContainerDark,
    onPrimaryContainer = FinOnPrimaryContainerDark,
    secondary = FinSecondaryDark,
    onSecondary = FinOnSecondaryDark,
    secondaryContainer = FinSecondaryContainerDark,
    onSecondaryContainer = FinOnSecondaryContainerDark,
    tertiary = FinTertiaryDark,
    onTertiary = FinOnTertiaryDark,
    tertiaryContainer = FinTertiaryContainerDark,
    onTertiaryContainer = FinOnTertiaryContainerDark,
    background = FinBackgroundDark,
    onBackground = FinOnBackgroundDark,
    surface = FinSurfaceDark,
    onSurface = FinOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = FinPrimaryLight,
    onPrimary = FinOnPrimaryLight,
    primaryContainer = FinPrimaryContainerLight,
    onPrimaryContainer = FinOnPrimaryContainerLight,
    secondary = FinSecondaryLight,
    onSecondary = FinOnSecondaryLight,
    secondaryContainer = FinSecondaryContainerLight,
    onSecondaryContainer = FinOnSecondaryContainerLight,
    tertiary = FinTertiaryLight,
    onTertiary = FinOnTertiaryLight,
    tertiaryContainer = FinTertiaryContainerLight,
    onTertiaryContainer = FinOnTertiaryContainerLight,
)

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
