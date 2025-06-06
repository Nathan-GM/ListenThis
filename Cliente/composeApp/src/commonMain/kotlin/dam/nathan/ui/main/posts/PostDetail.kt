package dam.nathan.ui.main.posts

import VideoPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.imageLoader
import dam.nathan.models.Comments
import dam.nathan.models.Genre
import dam.nathan.models.Likes
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.posts.comments.CommentsMain
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PostDetail(
    post: Post,
    userVM: UserViewModel,
    genre: Genre?,
    volver: () -> Unit,
    goToAuthorProfile: (String) -> Unit,
) {
    var postVM: PostViewModel = koinViewModel()
    var author by remember { mutableStateOf("") }
    var waiting by remember { mutableStateOf(false) }
    var firstTry by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }
    var commentSize by remember { mutableStateOf(post.comments?.size ?: 0) }

    var comment by remember { mutableStateOf("") }

    var postDate = getTimeOfPost(post.timestamp)
    var containerColor = MaterialTheme.colorScheme.secondaryContainer

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var photoSize = 0
    var videoHeight = 0

    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        photoSize = 250
        videoHeight = 250
    } else {
        photoSize = 210
        videoHeight = 210
    }


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
        containerColor = Color(genre.color[0], genre.color[1], genre.color[2])
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${post!!.title} - $author - $postDate", fontSize = 25.sp) },
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
            BottomAppBar(modifier = Modifier.height(72.dp)) {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Comentario a publicar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val newComment = Comments(
                                            authorId = userVM.user.value.user!!.id!!,
                                            comment = comment,
                                            timeOfPost = System.currentTimeMillis()
                                        )
                                        post.comments!!.add(newComment)
                                        commentSize = post.comments.size
                                        postVM.updatePost(post, userVM.user.value.token!!)
                                        comment = ""
                                    }
                                    println(commentSize)
                                },
                                enabled = comment.isNotEmpty()
                            ) {
                                Icon(
                                    Icons.Filled.Send,
                                    ""
                                )
                            }
                        }
                    )
                }
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
                        modifier = Modifier.height(if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 250.dp else 350.dp)
                            .fillMaxWidth()
                    ) {
                        if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                            Row {
                                if (post.media != null && post.media != "") {
                                    imageLoader(post.media, photoSize)
                                }
                                Spacer(Modifier.width(10.dp))
                                if (post.ytURL != null && post.ytURL != "") {
                                    VideoPlayer(
                                        Modifier.width(500.dp).height(videoHeight.dp)
                                            .background(containerColor),
                                        post.ytURL,
                                    )

                                }
                            }
                        } else {
                            if (post.media != null && post.media != "") {
                                imageLoader(post.media, photoSize)
                            }

                            Spacer(Modifier.height(10.dp))
                            if (post.ytURL != null && post.ytURL != "") {
                                println(post.ytURL)
                                VideoPlayer(
                                    Modifier.fillMaxWidth().height(videoHeight.dp)
                                        .background(containerColor),
                                    post.ytURL,
                                )

                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row {
                            Text(
                                text = "${author}: ",
                                fontSize = 18.sp,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable { goToAuthorProfile(post.author) })
                            Text(text = "${post.content}", fontSize = 18.sp)
                        }
                        Spacer(Modifier.height(25.dp))
                        Row(
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    waiting = true
                                    scope.launch {
                                        if (post.likes!!.isEmpty() || !post.likes!!.contains(
                                                Likes(
                                                    userVM.user.value.user!!.id!!
                                                )
                                            )
                                        ) {
                                            post.likes.add(Likes(userVM.user.value.user!!.id!!))
                                        } else if (post.likes!!.contains(Likes(userVM.user.value.user!!.id!!))) {
                                            post.likes.remove(Likes(userVM.user.value.user!!.id!!))
                                        }

                                        val result =
                                            postVM.updatePost(post, userVM.user.value.token!!)

                                        if (result == "error") {
                                            error = true
                                        } else {
                                            error = false
                                        }
                                    }
                                    waiting = false
                                }
                            ) {
                                Icon(
                                    Icons.Filled.MusicNote,
                                    "",
                                    tint = if (post.likes!!.contains(Likes(userId = userVM.user.value.user!!.id!!))) Color.Red else Color.White
                                )
                            }

                            if (error) {
                                Text(
                                    text = "Ha ocurrido un problema al dar like al post, intentalo más tarde",
                                    color = if (genre != null && genre.name != "Rock") MaterialTheme.colorScheme.error else Color.White
                                )
                            }
                        }
                    }
                }
            }
            if (!waiting) {
                LazyColumn(
                    modifier = Modifier.
                    fillMaxSize().
                    background(MaterialTheme.colorScheme.secondaryContainer).
                    padding(bottom = 72.dp).
                    consumeWindowInsets(PaddingValues(bottom = 72.dp))

                ) {
                    if (commentSize > 0) {
                        items(commentSize) {
                            CommentsMain(
                                comment = post.comments!![it],
                                userVM = userVM
                            )
                            //Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

            }
        }
    }
}

fun getTimeOfPost(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val formatedDate = formatter.format(date)
    val splitedDate = formatedDate.split(" ")

    return "${splitedDate[0]} - ${splitedDate[1]}"
}