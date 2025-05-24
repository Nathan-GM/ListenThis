package dam.nathan.ui.main.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import dam.nathan.imageLoader
import dam.nathan.models.Genre
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.log

@Composable
fun PostCard(
    userVM: UserViewModel,
    post: Post,
    genre: Genre?,
    goToPostDetail: (Post, UserViewModel) -> Unit,
) {
    val genreVM: GenreViewModel = koinViewModel()
    var author by remember { mutableStateOf("") }
    var waiting by remember { mutableStateOf(false) }
    var firstTry by remember { mutableStateOf(true) }

    var containerColor = MaterialTheme.colorScheme.secondaryContainer

    var scope = rememberCoroutineScope()

//    if (firstTry) {
//        waiting = true
//        scope.launch {
//            //TODO add get user by ID to get the name here
//            waiting = false
//            firstTry = false
//        }
//    }

    if (genre != null) {
        containerColor = Color(genre.color[0],genre.color[1],genre.color[2],)
    }

    if (waiting) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Card(
            modifier = Modifier.padding(16.dp).height(400.dp).width(300.dp)
                .clickable { goToPostDetail(post, userVM) },
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
                Text(post.author)
                if (post.media != null && post.media.isNotEmpty() && post.media != "") {
                    imageLoader(post.media, 200)
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(post.content)
            }

        }

    }

}