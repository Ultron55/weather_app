package example.weather.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DarkColorPalette = darkColorScheme(
    primary = LightOrange,
    secondary = BlackViolet,
    background = BlackViolet,
    onPrimary = White,
    onBackground = White,
    surface = BlackViolet,
    surfaceVariant = DarkGrayViolet,
    onSurface = White,
    onSecondary = White,
    onSurfaceVariant = White,
)

val LightColorPalette = lightColorScheme(
    primary = LightBlue1,
    secondary = Black,
    background = LightBlue1,
    onPrimary = Blue,
    onBackground = Blue,
    surface = LightBlue1,
    surfaceVariant = LightBlue2,
    onSurface = Blue,
    onSecondary = Blue,
    onSurfaceVariant = Blue,
)

@Composable
fun WeatherAppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            displaySmall = TextStyle(fontWeight = FontWeight. W100, fontSize = 96.sp ),
            labelLarge = TextStyle(fontWeight = FontWeight. W600, fontSize = 16.sp)
        ),
        shapes = Shapes(),
        content = content,
    )
}
