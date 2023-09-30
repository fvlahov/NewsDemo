package hr.vlahov.newsdemo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat

private val darkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = Color.Black,
    secondary = Green,
    onSecondary = Color.Black,
    tertiary = Green,
    onTertiary = Color.Black,
    background = DarkBlue,
    onBackground = Color.White,
    primaryContainer = DarkerBlue,
    onPrimaryContainer = LightBlue,
    secondaryContainer = LightBlue,
    onSecondaryContainer = Color.White,
    surface = Color.White
)

@Composable
fun NewsDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = darkColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme,
        typography = Typography,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                content = content
            )
        }
    )
}