package dam.nathan.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.Genre
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.configuration.SettingsPage
import dam.nathan.ui.main.genres.GenresPage
import dam.nathan.ui.main.main.MainPage
import dam.nathan.ui.main.posts.PostDetail
import dam.nathan.ui.main.posts.PostForm
import dam.nathan.ui.main.posts.PostsPage
import dam.nathan.ui.main.profile.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Destinations(
    val label: String,
    val icon: ImageVector,
    val description: String,
    val visibleCompact: Boolean,
    val visibleAtAll: Boolean,
) {
    MAIN("Inicio", Icons.Default.Home, "Main Screen", true, true),
    POSTS("Posts", Icons.Default.Public, "Main Screen", true, true), // TODO Change Icon
    GENRES("Géneros", Icons.Default.MusicNote, "Main Screen", true, true),
    PROFILE("Mi perfil", Icons.Default.Person, "Main Screen", true, true),
    CONFIG("Configuración", Icons.Default.Settings, "Main Screen", false, true),
    LOGOUT("Cerrar Sesión", Icons.Default.Logout, "Main Screen", true, true),
    CREATE("Crear post", Icons.Default.More, "Create", false, false),
    DETAILS("Crear post", Icons.Default.Details, "Post details", false, false),


}

@Preview
@Composable
fun MainScreen(
    goLogin: () -> Unit,
    userVM: UserViewModel,
    isDarkMode : Boolean,
    changeTheme: () -> Unit
) {

    var destinationSelected = remember { mutableStateOf(Destinations.MAIN) }
    var previousDestination = remember { mutableStateOf<Destinations?>(null) }
    var userSelected by remember { mutableStateOf<String?>(null) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var genrePicked by remember { mutableStateOf<Genre?>(null) }
    var detailsPost by remember { mutableStateOf<Post?>(null) }
    var isOnPostDetails by remember { mutableStateOf(false) }

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
                            MainPage(userVM.user.value, userVM, goToDetails = { post, genre ->
                                detailsPost = post
                                genrePicked = genre
                                destinationSelected.value = Destinations.DETAILS
                                previousDestination.value = Destinations.MAIN
                                isOnPostDetails = true
                            })
                            genrePicked = null
                            userSelected = null
                        }

                        Destinations.POSTS -> {
                            PostsPage(
                                userVM = userVM, genre = genrePicked,
                                goToCreate = {
                                    destinationSelected.value = Destinations.CREATE
                                },
                                goToDetails = { post, genre ->
                                    detailsPost = post
                                    genrePicked = genre
                                    destinationSelected.value = Destinations.DETAILS
                                    previousDestination.value = Destinations.POSTS
                                    isOnPostDetails = true
                                }
                            )
                            userSelected = null
                        }

                        Destinations.GENRES -> {
                            GenresPage(goToPostsByGenre = { genre ->
                                destinationSelected.value = Destinations.POSTS
                                genrePicked = genre
                            })
                            genrePicked = null
                            userSelected = null
                        }

                        Destinations.PROFILE -> {
                            Profile(userVM = userVM, goToDetails = { post, genre ->
                                detailsPost = post
                                genrePicked = genre
                                destinationSelected.value = Destinations.DETAILS
                                previousDestination.value = Destinations.POSTS
                                isOnPostDetails = true
                            }, userId = userSelected,
                                goToSettings = {
                                    destinationSelected.value = Destinations.CONFIG
                                })
                            genrePicked = null
                        }

                        Destinations.CONFIG -> {
                            SettingsPage(userVM = userVM, isDarkModeOn = isDarkMode, changeTheme = changeTheme)
                            genrePicked = null
                            userSelected = null
                        }

                        Destinations.LOGOUT -> {
                            genrePicked = null
                            userSelected = null
                            userVM.logut()
                            goLogin()
                        }

                        Destinations.CREATE -> {
                            PostForm(
                                user = userVM.getUser(),
                                volver = {
                                    destinationSelected.value = Destinations.POSTS
                                }
                            )
                            userSelected = null
                        }

                        Destinations.DETAILS -> {
                            PostDetail(post = detailsPost!!, genre = genrePicked,
                                userVM = userVM, volver = {
                                    genrePicked = null
                                    userSelected = null
                                    destinationSelected.value =
                                        previousDestination.value ?: Destinations.MAIN
                                    isOnPostDetails = false
                                }, goToAuthorProfile = { author ->
                                    userSelected = author
                                    destinationSelected.value = Destinations.PROFILE
                                    isOnPostDetails = false
                                })
                        }
                    }
                }
            }
        }
    }

}