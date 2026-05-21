package pe.ripc.superheroapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EmeraldHeroColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = TextPrimary,
    secondary = TealSecondary,
    onSecondary = BackgroundMain,
    tertiary = GoldImpact,
    onTertiary = BackgroundMain,
    background = BackgroundMain,
    onBackground = TextPrimary,
    surface = BackgroundSecondary,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderSutil,
    error = StatusError,
    onError = TextPrimary
)

@Composable
fun SuperheroAppTheme(
    darkTheme: Boolean = true, // Forzado a dark mode como indica el prompt
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = EmeraldHeroColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
