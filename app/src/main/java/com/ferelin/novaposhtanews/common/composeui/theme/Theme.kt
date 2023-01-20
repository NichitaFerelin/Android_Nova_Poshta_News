package com.ferelin.novaposhtanews.common.composeui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun NovaPoshtaNewsTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightAndroidColorScheme,
        typography = NovaPoshtaNewsTypography,
        shapes = NovaPoshtaNewsShapes,
        content = content,
    )
}

val LightAndroidColorScheme = lightColorScheme(
    primary = Black1,
    onPrimary = Black1,
    primaryContainer = White2,
    onPrimaryContainer = Black1,
    inversePrimary = White1,
    secondary = Gray1,
    onSecondary = Black1,
    secondaryContainer = White2,
    onSecondaryContainer = Black1,
    tertiary = Green1,
    onTertiary = White2,
    tertiaryContainer = White2,
    onTertiaryContainer = White2,
    background = White1,
    onBackground = White2,
    surface = Blue2,
    onSurface = Black1,
    surfaceVariant = Blue2,
    onSurfaceVariant = Black1,
    surfaceTint = Blue1,
    inverseSurface = Red1,
    inverseOnSurface = Red1,
    error = Red1,
    onError = White1,
    errorContainer = White2,
    onErrorContainer = White1,
    outline = Blue2,
    outlineVariant = Blue2,
    scrim = White2.copy(alpha = 0.7f),
)
