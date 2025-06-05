package dam.nathan.ui.main.posts.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dam.nathan.models.Comments
import dam.nathan.models.viewmodels.UserViewModel
import dam.nathan.ui.main.posts.getTimeOfPost
import kotlinx.coroutines.launch

@Composable
fun CommentsMain(
    comment: Comments,
    userVM: UserViewModel
) {
    val scope = rememberCoroutineScope()
    var author by remember { mutableStateOf<String>("") }
    val dateOfComment = getTimeOfPost(comment.timeOfPost)

    var waiting by remember { mutableStateOf(false) }
    var firstTime by remember { mutableStateOf(true) }

    if (firstTime) {
        waiting = true
        scope.launch {
            var tmp = userVM.getUserById(comment.authorId)
            if (tmp != null) {
                author = tmp.username
            }
            waiting = false
            firstTime = false
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (waiting) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp).align(Alignment.CenterHorizontally)
            )
        } else {
            Box(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().border(5.dp, MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text("${author} - $dateOfComment")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(comment.comment)
                }
            }
        }
    }

}