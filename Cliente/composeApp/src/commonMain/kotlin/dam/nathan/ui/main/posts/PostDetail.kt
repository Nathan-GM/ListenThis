package dam.nathan.ui.main.posts

import VideoPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.nathan.imageLoader
import dam.nathan.models.Genre
import dam.nathan.models.Likes
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import listenthis.composeapp.generated.resources.Res
import listenthis.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable fun PostDetail(
    post: Post,
    userVM : UserViewModel,
    genre: Genre?,
    volver: () -> Unit
) {
    var postVM : PostViewModel = koinViewModel()
    var author by remember { mutableStateOf("") }
    var waiting by remember { mutableStateOf(false) }
    var firstTry by remember { mutableStateOf(true) }

    var postDate = getTimeOfPost(post.timestamp)
    var containerColor = MaterialTheme.colorScheme.secondaryContainer



    var scope = rememberCoroutineScope()

    if (firstTry) {
        print(post)
        waiting = true
        scope.launch {
            val tmpUser = userVM.getUserById(post.author)
            if (tmpUser != null) {
                author = tmpUser.username
            }
            waiting = false
            firstTry = false
        }
    }

    if (genre != null) {
        containerColor = Color(genre.color[0],genre.color[1],genre.color[2],)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${post!!.title} - $author - $postDate", fontSize = 25.sp)},
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ), actions = {
                    IconButton(
                        onClick = { volver() }
                    ) {
                        Icon(
                            Icons.Default.NavigateBefore,
                            contentDescription = ""
                        )
                    }
                }
            )
        }, bottomBar = {
            BottomAppBar {
                Text("WIP - Escribir comentarios asd")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardColors(
                        contentColor = Color.White,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        containerColor = containerColor
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.height(500.dp).fillMaxWidth()
                    ) {
                        Row {
                            if (post.media != null && post.media != "") {
                                imageLoader(post.media, 400)
                            }
                            Spacer(Modifier.width(10.dp))
                            if (post.ytURL != null && post.ytURL != "") {
                                println(post.ytURL)
                                VideoPlayer(
                                    Modifier.fillMaxWidth().height(400.dp).background(containerColor),
                                    post.ytURL,
                                )

                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(text = post.content, fontSize = 18.sp)
                        Spacer(Modifier.height(25.dp))
                        Row(
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    println("tmp")
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    "",
                                    tint = if (post.likes!!.contains(Likes(userId = userVM.user.value.user!!.id!!))) Color.Red else Color.White
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Magenta)
                )
            }
        }
    }
}

fun getTimeOfPost(timestamp : Long) : String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val formatedDate = formatter.format(date)
    val splitedDate = formatedDate.split(" ")

    return "${splitedDate[0]} - ${splitedDate[1]}"
}