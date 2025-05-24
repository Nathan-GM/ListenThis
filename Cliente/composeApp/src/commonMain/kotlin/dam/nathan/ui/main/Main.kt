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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.Genre
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.genres.GenresPage
import dam.nathan.ui.main.main.MainPage
import dam.nathan.ui.main.posts.PostForm
import dam.nathan.ui.main.posts.PostsPage
import dam.nathan.ui.main.profile.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

enum class Destinations(
    val label: String,
    val icon: ImageVector,
    val description:String,
    val visibleCompact: Boolean,
    val visibleAtAll: Boolean
) {
    MAIN("Inicio", Icons.Default.Home, "Main Screen", true, true),
    POSTS("Posts", Icons.Default.Public, "Main Screen", true, true), // TODO Change Icon
    GENRES("Géneros", Icons.Default.MusicNote, "Main Screen", true, true),
    PROFILE("Mi perfil", Icons.Default.Person, "Main Screen", true, true),
    CONFIG("Configuración", Icons.Default.Settings, "Main Screen", false, true),
    LOGOUT("Cerrar Sesión", Icons.Default.Logout, "Main Screen", true, true),
    CREATE("Crear post", Icons.Default.More, "Create", false, false)


}

@Preview
@Composable
fun MainScreen(
    goLogin: () -> Unit,
    userVM : UserViewModel
) {

    var destinationSelected = remember { mutableStateOf(Destinations.MAIN) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var genrePicked by remember { mutableStateOf<Genre?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        NavigationSuiteScaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            navigationSuiteItems = {
                Destinations.entries.forEach {
                    if (it.visibleAtAll) {
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
                                },

                                )
                        }
                    }
                }
            },
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
                            MainPage(userVM.user.value)
                            genrePicked = null
                        }
                        Destinations.POSTS -> {
                            PostsPage(userVM = userVM, genre = genrePicked, goToCreate = {
                                destinationSelected.value = Destinations.CREATE
                            })
                        }
                        Destinations.GENRES -> {
                            GenresPage(goToPostsByGenre = { genre ->
                                destinationSelected.value = Destinations.POSTS
                                genrePicked = genre
                            })
                            genrePicked = null
                        }
                        Destinations.PROFILE -> {
                            Profile(userVM = userVM)
                            genrePicked = null
                        }
                        Destinations.CONFIG -> {
                            println("CONFIG")
                            genrePicked = null
                        }
                        Destinations.LOGOUT -> {
                            genrePicked = null
                            goLogin()
                        }
                        Destinations.CREATE -> {
                            PostForm(
                                user = userVM.getUser(),
                                volver = {
                                    destinationSelected.value = Destinations.POSTS
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}