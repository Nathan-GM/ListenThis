package dam.nathan.ui.main.genres

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import dam.nathan.models.viewmodels.GenreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresPage(
    goToPostsByGenre : (Genre) -> Unit
) {
    val genreVM: GenreViewModel = koinViewModel()
    var genres by remember { mutableStateOf(mutableListOf<Genre>())  }
    val scope = rememberCoroutineScope()
    var waiting by remember { mutableStateOf(false) }
    var firstTry by remember { mutableStateOf(true) }
    var genreSize by remember { mutableStateOf(0) }

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
                title = { Text("Géneros existentes: $genreSize") },
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
                    val tmp = genreVM.getAllGenres()
                    if (tmp == null) {
                        delay(2000)
                        firstTry = false
                        waiting = false
                    } else {
                        delay(2000)
                        genres = tmp
                        firstTry = false
                        waiting = false
                        genreSize = genres.size
                    }
                }
            }

            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                ) {
                    items(genreSize) { genre ->
                        GenreCard(
                            genres[genre],
                            goPostList = { goToGenre -> goToPostsByGenre(goToGenre) }
                        )
                    }
                }
            }
        }
    }
}