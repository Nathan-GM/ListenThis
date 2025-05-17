package dam.nathan

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam.nathan.models.User
import dam.nathan.models.repositories.UserRepository
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.login.LoginScreen
import dam.nathan.ui.main.MainScreen
import dam.nathan.ui.register.RegisterScreen
import dam.nathan.ui.theme.AppTheme
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.internal.ClasspathHelper
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var dark = remember { mutableStateOf(true) }
    AppTheme(
        dark = dark.value,
    ) {
        val navController = rememberNavController()
        val vm : UserViewModel = UserViewModel(UserRepository())

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    isDarkModeOn = dark.value,
                    register = {
                        navController.navigate("register")
                    },
                    login = {
                        username, password ->
                        val user = User(username = username, password = password)
                        val result = vm.login(user)
                        if (result.equals("valid")) {
                            vm.setUser(user)
                            navController.navigate("main")
                            "valid"
                        } else {
                            if (result.equals("timeout")) {
                                "timeout"
                            } else {
                                "error"
                            }
                        }
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