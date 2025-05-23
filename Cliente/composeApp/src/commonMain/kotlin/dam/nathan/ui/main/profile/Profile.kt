package dam.nathan.ui.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.Post
import dam.nathan.models.User
import listenthis.composeapp.generated.resources.Res
import listenthis.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Profile(

) {
// TMP Data
    val user = User(username = "Test", password = "", avatar = "a")
    val postsByUser = mutableMapOf<String, List<Post>>()
    val genre = "Pop"

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var spacer = 0
    var photoSize = 0
    var postText = ""
    var genreText = ""
    var startEndPadding = 0
    var topBottomPadding = 0

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
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de ${user.username}") },
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
        Card(
            modifier = Modifier
                .padding(startEndPadding.dp, topBottomPadding.dp, startEndPadding.dp, topBottomPadding.dp)
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
                if (user.avatar != null) {
                    if (user.avatar.isNotEmpty()) {
                        //val avatar = Base64.decode(user.avatar.toByteArray())
                        //val bitmap = ImageIO.read(ByteArrayInputStream(avatar)).toPainter()
                        Image(
                            //bitmap,
                            painterResource(Res.drawable.compose_multiplatform), //Used for debug, remove later
                            contentDescription = "${user.username} icon",
                            Modifier.clip(RoundedCornerShape(8.dp)).size(photoSize.dp)
                        )
                    }
                }

                Spacer(Modifier.width(spacer.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "${user.username}")
                    Spacer(Modifier.width(spacer.dp))
                    Text(postText)
                    Spacer(Modifier.width(spacer.dp))
                    Text(genreText, textAlign = TextAlign.Center)
                }
            }
        }
    }
}