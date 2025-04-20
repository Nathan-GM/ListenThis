package dam.nathan.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(
    dark: Boolean,
    content: @Composable () -> Unit
) {
    val isSystemDarkTheme = isSystemInDarkTheme()
    val colorScheme = when {
        (dark == true || isSystemDarkTheme == true) -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}