package dam.nathan.ui.main.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.Genre
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsPage(
    userVM: UserViewModel,
    genre: Genre? = null,
    goToCreate: () -> Unit,
    goToDetails: (Post, Genre?) -> Unit,
) {
    val postVM: PostViewModel = koinViewModel()
    var posts by remember { mutableStateOf(mutableListOf<Post>()) }
    var postByGenre by remember { mutableStateOf(mutableListOf<Post>()) }
    var postsSize by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var waiting by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var firstTry by remember { mutableStateOf(true) }
    var selectedGenre by remember { mutableStateOf<Genre?>(genre) }

    var genreVM: GenreViewModel = koinViewModel()
    var genres by remember { mutableStateOf<MutableList<Genre>>(mutableListOf()) }
    var buttonText by remember { mutableStateOf("") }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var startEndPadding = 0
    var topBottomPadding = 0

    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        startEndPadding = 16
        topBottomPadding = 75
    } else {
        startEndPadding = 14
        topBottomPadding = 120
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicaciones") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }, bottomBar = {
            BottomAppBar {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!waiting) {
                        if (genres.size > 0) {
                            GenreComboBox(
                                selectedGenre,
                                genres,
                                cambio = {
                                    selectedGenre = it
                                    waiting = true
                                    firstTry = true
                                },
                                inForm = false
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            if (selectedGenre != null && !waiting) {
                                if (userVM.user.value.user!!.followedGenres!!.contains(selectedGenre!!.id)) {
                                    buttonText = "Dejar de seguir"
                                } else {
                                    buttonText = "Seguir"
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                waiting = true
                                                var result =
                                                    userVM.followGenre(selectedGenre!!.id!!)
                                                println(result)
                                                if (result == "ok") {
                                                    waiting = false
                                                } else if (result == "timeout") {
                                                    errorText =
                                                        "Error al conectarse con el servidor"
                                                    error = true
                                                    waiting = false
                                                } else if (result == "error") {
                                                    errorText =
                                                        "Error al seguir el género ${selectedGenre!!.name}"
                                                    error = true
                                                    waiting = false
                                                }
                                            }
                                        }
                                    ) {

                                        Text("$buttonText ${selectedGenre?.name}")

                                    }
                                    if (error) {
                                        Text(errorText)
                                    }
                                }
                                Button(
                                    onClick = {
                                        waiting = true
                                        firstTry = true
                                        selectedGenre = null
                                    }
                                ) {
                                    Text("Dejar de filtrar")
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    goToCreate()
                }
            ) {
                Icon(
                    Icons.Filled.PlusOne,
                    ""
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(
                startEndPadding.dp,
                topBottomPadding.dp,
                startEndPadding.dp,
                topBottomPadding.dp
            )
        ) {
            if (firstTry) {
                waiting = true
                scope.launch {
                    val tmp = postVM.getAllPosts()
                    val tmpG = genreVM.getAllGenres()
                    if (tmp == null) {
                        delay(2000)
                        firstTry = false
                        waiting = false
                    } else {
                        delay(2000)
                        posts = tmp
                        if (tmpG != null) {
                            genres = tmpG
                        }
                        if (selectedGenre != null) {
                            postsSize = posts.count { x -> x.genre == selectedGenre?.id }
                            postByGenre =
                                posts.filter { x -> x.genre == selectedGenre?.id }.toMutableList()
                        } else {
                            postsSize = tmp.size
                        }
                    }
                    firstTry = false
                    waiting = false

                }
            }

            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                )
            } else {
                if (postsSize > 0) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 200.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        items(postsSize) { post ->
                            if (selectedGenre != null) {
                                if (postByGenre[post].genre == selectedGenre!!.id) {
                                    PostCard(
                                        userVM = userVM,
                                        post = postByGenre[post],
                                        genre = selectedGenre,
                                        goToPostDetail = { detail -> goToDetails(detail, genre) }
                                    )
                                }
                            } else {
                                val postG = genres.find { it.id == posts[post].genre }
                                PostCard(
                                    userVM = userVM,
                                    post = posts[post],
                                    genre = postG,
                                    goToPostDetail = { detail -> goToDetails(detail, postG) }
                                )
                            }
                        }
                    }
                } else {
                    Text("No se han encontrado publicaciones. \n ¡Prueba a crear una!")
                }
            }
        }
    }
}