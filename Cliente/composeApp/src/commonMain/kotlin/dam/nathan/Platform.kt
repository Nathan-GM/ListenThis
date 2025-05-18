package dam.nathan

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun ib64(
    onChange: (String) -> Unit,
    initString: String? = null
)

@Composable
expect fun darkMode(changeTheme: () -> Unit, isDarkModeOn: Boolean)