package dam.nathan

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam.nathan.ui.login.LoginScreen
import dam.nathan.ui.main.MainScreen
import dam.nathan.ui.register.RegisterScreen
import dam.nathan.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var dark = remember { mutableStateOf(true) }
    AppTheme(
        dark = dark.value,
    ) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    isDarkModeOn = dark.value,
                    register = {
                        navController.navigate("register")
                        println("TMP - REGISTER FUNCTION")
                    },
                    login = {
                        navController.navigate("main")
                        println("TMP - LOGIN FUNCTION")
                    },
                    changeTheme = {
                        dark.value = !dark.value
                    }
                )
            }

            composable("register") {
                RegisterScreen(
                    goLogin = {
                        navController.navigate("login")
                    }
                )
            }

            composable("main") {
                MainScreen(
                    goLogin = {
                        navController.navigate("login")
                    }
                )
            }

        }

    }
}