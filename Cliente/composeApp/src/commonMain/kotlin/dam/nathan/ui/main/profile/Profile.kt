package dam.nathan.ui.main.profile

//import androidx.compose.ui.graphics.toPainter
//import androidx.compose.ui.graphics.toPainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.imageLoader
import dam.nathan.models.Post
import dam.nathan.models.viewmodels.UserViewModel
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    userVM: UserViewModel,
) {
// TMP Data
    val user = userVM.user.value
    val postsByUser = mutableListOf<Post>()
    val genre = "Pop"

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
        topBottomPadding = 120
        configButton = true
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de ${user.user?.username}") },
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
        Card(
            modifier = Modifier
                .padding(
                    startEndPadding.dp,
                    topBottomPadding.dp,
                    startEndPadding.dp,
                    topBottomPadding.dp
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
                if (user.user?.avatar != null) {
                    if (user.user.avatar.isNotEmpty()) {
                        imageLoader(user.user.avatar, photoSize)

//                        val avatar = Base64.decode(user.user.avatar.toByteArray())
//                        val bitmap = ImageIO.read(ByteArrayInputStream(avatar)).toPainter()
//                        Image(
//                            bitmap,
//                            contentDescription = "${user.user?.username} icon",
//                            Modifier.clip(RoundedCornerShape(8.dp)).size(photoSize.dp)
//                        )
                    }
                }

                Spacer(Modifier.width(spacer.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "${user.user?.username}")
                    Spacer(Modifier.width(spacer.dp))
                    Text(postText)
                    Spacer(Modifier.width(spacer.dp))
                    Text(genreText, textAlign = TextAlign.Center)
                }
            }
        }
    }
}