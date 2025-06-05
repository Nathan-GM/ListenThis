package dam.nathan.ui.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.imageLoader
import dam.nathan.models.Genre
import dam.nathan.models.Post
import dam.nathan.models.User
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.posts.PostCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    userVM: UserViewModel,
    goToDetails: (Post, Genre?) -> Unit,
    userId: String? = null
) {
    var user by remember { mutableStateOf<User?>(userVM.user.value.user) }
    var postsByUser by remember { mutableStateOf(mutableListOf<Post>()) }

    val postVM: PostViewModel = koinViewModel()
    val genreVM: GenreViewModel = koinViewModel()
    var genres by remember { mutableStateOf<MutableList<Genre>>(mutableListOf()) }

    var scope = rememberCoroutineScope()
    var waiting by remember { mutableStateOf(false) }
    var firsTry by remember { mutableStateOf(true) }

    var genre by remember { mutableStateOf("") }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var spacer = 0
    var photoSize = 0
    var postText = ""
    var genreText = ""
    var startEndPadding = 0
    var topBottomPadding = 0
    var configButton = false

    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        spacer = 30
        photoSize = 150
        postText = "Publicaciones hechas: ${postsByUser.size}"
        genreText = "Género más públicado: $genre"
        startEndPadding = 16
        topBottomPadding = 75

    } else {
        spacer = 15
        photoSize = 70
        postText = "Posts: \n ${postsByUser.size}"
        genreText = "Género principal: \n $genre"
        startEndPadding = 14
        topBottomPadding = 130
        configButton = true
    }

    if (firsTry) {
        waiting = true
        scope.launch {
            if (userId == null) {
                var tmp = postVM.getUserPosts(user!!)
                var tmpG = genreVM.getAllGenres()
                if (tmp.isNotEmpty()) {
                    postsByUser = tmp
                    if (tmpG != null) {
                        genres = tmpG
                    }
                    var tmpPG = postsByUser.groupingBy { it.genre }.eachCount().toSortedMap()

                    println(tmpPG)
                    genre = genres.find { it.id == tmpPG.maxBy { it.value }.key }?.name ?: ""
                }
            } else {
                user = userVM.getUserById(userId)
                if (user != null) {
                    var PBU = postVM.getUserPosts(user!!)
                    var tmpG = genreVM.getAllGenres()
                    if (PBU.isNotEmpty()) {
                        postsByUser = PBU
                        if (tmpG != null) {
                            genres = tmpG
                        }
                        var tmpPG = postsByUser.groupingBy { it.genre }.eachCount().toSortedMap()

                        println(tmpPG)
                        genre = genres.find { it.id == tmpPG.maxBy { it.value }.key }?.name ?: ""
                    }
                }
            }
            delay(2000)
            waiting = false
            firsTry = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de ${user!!.username}")  },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ), actions = {
                    if (configButton) {
                        IconButton(
                            enabled = configButton,
                            onClick = { println("settings") } // TODO go to settings screen
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "",
                            )
                        }
                    }
                }
            )
        }
    ) {
        if (waiting) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (waiting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(
                            top = topBottomPadding.dp,
                            start = startEndPadding.dp,
                            end = startEndPadding.dp,
//                            bottom = topBottomPadding.dp
                        )
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (user!!.avatar != null) {
                            if (user!!.avatar!!.isNotEmpty()) {
                                imageLoader(user!!.avatar!!, photoSize)
                            }
                        }

                        Spacer(Modifier.width(spacer.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = "${user!!.username}")
                            Spacer(Modifier.width(spacer.dp))
                            Text(postText)
                            Spacer(Modifier.width(spacer.dp))
                            Text(genreText, textAlign = TextAlign.Center)
                        }
                    }

                    Spacer(modifier = Modifier.height(spacer.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(user!!.biography ?: "", textAlign = TextAlign.Center)
                    }
                }


                if (postsByUser.size > 0) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 200.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(postsByUser.size) {
                            val postG = genres.find { x -> x.id == postsByUser[it].genre }
                            PostCard(
                                userVM = userVM,
                                post = postsByUser[it],
                                genre = postG,
                                goToPostDetail = { detail -> goToDetails(postsByUser[it], postG) }
                            )
                        }
                    }
                } else {
                    Text("Ahora mismo el usuario ${user!!.username} no cuenta con publicaciones.")
                }


            }
        }
    }
}
