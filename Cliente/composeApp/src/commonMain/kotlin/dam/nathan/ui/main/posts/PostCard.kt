package dam.nathan.ui.main.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.imageLoader
import dam.nathan.models.Genre
import dam.nathan.models.Likes
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PostCard(
    userVM: UserViewModel,
    post: Post,
    genre: Genre?,
    goToPostDetail: (Post) -> Unit,
) {
    var author by remember { mutableStateOf("") }
    var waiting by remember { mutableStateOf(false) }
    var firstTry by remember { mutableStateOf(true) }

    var containerColor = MaterialTheme.colorScheme.secondaryContainer

    var scope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var cardHeigh = 0
    var cardWidth = 0
    var pictureSize = 0

    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        cardHeigh = 320
        cardWidth = 300
        pictureSize = 200
    } else {
        cardHeigh = 150
        cardWidth = 100
        pictureSize = 100
    }

    if (firstTry) {
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

    if (waiting) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Card(
            modifier = Modifier.padding(16.dp).wrapContentHeight().width(cardWidth.dp)
                .clickable { goToPostDetail(post) },
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardColors(
                contentColor = Color.White,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                containerColor = containerColor,
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text("Publicado por $author", fontSize = 10.sp)
                Spacer(modifier = Modifier.height(18.dp))
                if (post.media != null && post.media.isNotEmpty() && post.media != "") {
                    imageLoader(post.media, pictureSize)
                } else {
                    Spacer(modifier = Modifier.height(pictureSize.dp))
                }
                Row {
                    Icon(
                        Icons.Filled.MusicNote,
                        "",
                        tint = if (post.likes!!.contains(Likes(userVM.user.value.user!!.id!!))) Color.Red else Color.White,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("${post.likes.size}")

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        Icons.Filled.ModeComment,
                        "",
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("${post.comments!!.size}")

                }
            }

        }

    }

}