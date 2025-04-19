package dam.nathan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(dark: Boolean, content: @Composable () -> Unit) {
    val colorScheme = when {
        dark == true -> darkScheme
        else -> lightScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}