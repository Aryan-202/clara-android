package com.clara.agent.org.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Dark color scheme for the app.
 * Uses high contrast black/white colors.
 */
private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = Gray80,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    outline = Gray80
)

/**
 * Light color scheme for the app.
 * Uses high contrast black/white colors.
 */
private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = Gray20,
    onSecondary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    outline = Gray20
)

/**
 * App theme that applies the Clara color scheme and typography.
 *
 * Supports dynamic color on Android 12+ when [dynamicColor] is `true`.
 *
 * @param darkTheme whether to use dark theme. Defaults to system setting.
 * @param dynamicColor whether to use dynamic color (Material You) if available.
 * @param content composable content to apply the theme to.
 */
@Composable
fun ClaraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}