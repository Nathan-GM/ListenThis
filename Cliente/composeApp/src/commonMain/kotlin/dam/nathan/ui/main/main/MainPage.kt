package dam.nathan.ui.main.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.Genre
import dam.nathan.models.Post
import dam.nathan.models.User
import dam.nathan.models.UserwithToken
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.posts.PostCard
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(user: UserwithToken, userVM: UserViewModel) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val postVM: PostViewModel = koinViewModel()
    val genreVM: GenreViewModel = koinViewModel()

    var genre by remember { mutableStateOf<Genre?>(null) }
    var secondGenre by remember { mutableStateOf<Genre?>(null) }

    var latestPost by remember { mutableStateOf<Post?>(null) }
    var secondGenreLatestPost by remember { mutableStateOf<Post?>(null) }

    var recommended by remember { mutableStateOf<MutableList<Post>>(mutableListOf()) }

    var waiting by remember { mutableStateOf(false) }
    var firstTime by remember { mutableStateOf(true) }
    var scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ListenThis") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }
    ) {
        if (firstTime) {
            waiting = true
            scope.launch {
                val tmpPost = postVM.getAllPosts()
                val tmpGenres = genreVM.getAllGenres()
                var same = true
                if (user.user!!.followedGenres!!.isNotEmpty()) {
                    if (tmpGenres != null && tmpPost != null) {
                        tmpPost.sortByDescending { it.timestamp }
                        val randomGenre = user.user!!.followedGenres!!.random()
                        genre = tmpGenres.find { x -> x.id == randomGenre }

                        if (user.user!!.followedGenres!!.size > 1) {
                            while (same) {
                                val randomGenre2 = user.user!!.followedGenres!!.random()
                                secondGenre = tmpGenres.find { x -> x.id == randomGenre2 }
                                if (secondGenre != genre) {
                                    secondGenreLatestPost =
                                        tmpPost.first { x -> x.genre == secondGenre!!.id }
                                    same = false
                                    recommended.add(secondGenreLatestPost!!)
                                }
                            }
                        }

                        latestPost = tmpPost.first { x -> x.genre == genre!!.id }
                        recommended.add(latestPost!!)
                    }
                }
                waiting = false
                firstTime = false
            }
        }
        // TODO Revisar esto
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "Bienvenido a ListenThis ${user.user!!.username}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (recommended.isNotEmpty() || latestPost != null) {
                Text(
                    text = "Recomendaciones",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (!waiting) {
                    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            if (latestPost != null && genre != null) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.height(350.dp)
                                ) {
                                    Text("Ultima públicación del género ${genre!!.name}")
                                    PostCard(
                                        userVM = userVM,
                                        post = latestPost!!,
                                        genre = genre,
                                        goToPostDetail = { println(latestPost) }
                                    )
                                }

                            }
                            if (secondGenreLatestPost != null && secondGenre != null) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.height(350.dp)
                                ) {
                                    Text("Ultima públicación del género ${secondGenre!!.name}")
                                    PostCard(
                                        userVM = userVM,
                                        post = secondGenreLatestPost!!,
                                        genre = secondGenre,
                                        goToPostDetail = { println(secondGenreLatestPost) }
                                    )
                                }
                            }
                        }
                    } else {
                        if (recommended.size > 0 && recommended.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 250.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                items(recommended.size) {
                                    if (recommended[it].genre == genre!!.id) {
                                        PostCard(
                                            userVM = userVM,
                                            post = recommended[it],
                                            genre = genre!!,
                                            goToPostDetail = { detail -> println(detail) } // TODO Change this to go to detial
                                        )
                                    } else if (secondGenre != null) {
                                        if (recommended[it].genre == secondGenre!!.id) {
                                            PostCard(
                                                userVM = userVM,
                                                post = recommended[it],
                                                genre = secondGenre!!,
                                                goToPostDetail = { detail -> println(detail) } // TODO Change this to go to detial
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
