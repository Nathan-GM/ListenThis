package dam.nathan

import androidx.compose.runtime.*
import dam.nathan.ui.login.LoginScreen
import dam.nathan.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var dark = remember { mutableStateOf(true) }
    AppTheme(
        dark = dark.value,
    ) {
        LoginScreen(
            register = {
                println("TMP - REGISTER FUNCTION")
            },
            login = {
                println("TMP - LOGIN FUNCTION")
            },
            changeTheme = {
                dark.value = !dark.value
            }
        )
    }
}