package dam.nathan.ui.main.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.models.User
import dam.nathan.models.UserwithToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(user: UserwithToken) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Bienvenido a ListenThis ${user.user!!.username}",
                color = MaterialTheme.colorScheme.onBackground,
                style = if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}