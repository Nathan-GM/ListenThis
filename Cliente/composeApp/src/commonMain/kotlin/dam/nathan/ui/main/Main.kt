package dam.nathan.ui.main

/*import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector*/

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.main.MainPage
import dam.nathan.ui.main.profile.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

enum class Destinations(
    val label: String,
    val icon: ImageVector,
    val description:String,
    val visibleCompact: Boolean
) {
    MAIN("Inicio", Icons.Default.Home, "Main Screen", true),
    POSTS("Posts", Icons.Default.Public, "Main Screen", true), // TODO Change Icon
    GENRES("Géneros", Icons.Default.MusicNote, "Main Screen", true),
    PROFILE("Mi perfil", Icons.Default.Person, "Main Screen", true),
    CONFIG("Configuración", Icons.Default.Settings, "Main Screen", false),
    LOGOUT("Cerrar Sesión", Icons.Default.Logout, "Main Screen", true),


}

@Preview
@Composable
fun MainScreen(
    goLogin: () -> Unit,
) {

    var destinationSelected = remember { mutableStateOf(Destinations.MAIN) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var userVM : UserViewModel = koinViewModel()

    println(userVM.user)

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        NavigationSuiteScaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            navigationSuiteItems = {
                Destinations.entries.forEach {
                    if (it.visibleCompact == true || windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                        item(
                            icon = {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.description,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            label = {
                                Text(it.label)
                            },
                            selected = destinationSelected.value == it,
                            onClick = {
                                if (it == Destinations.LOGOUT) {
                                    goLogin()
                                } else {
                                    destinationSelected.value = it
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    when (destinationSelected.value) {
                        Destinations.MAIN -> {
                            MainPage()
                        }
                        Destinations.POSTS -> {
                            println("PUBLICACIONES")
                        }
                        Destinations.GENRES -> {
                            println("GÉNEROS")
                        }
                        Destinations.PROFILE -> {
                            Profile()
                        }
                        Destinations.CONFIG -> {
                            println("CONFIG")
                        }
                        Destinations.LOGOUT -> {
                            goLogin()
                        }
                    }
                }
            }
        }
    }

}