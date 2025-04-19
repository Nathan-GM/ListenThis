package dam.nathan.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    goLogin: () -> Unit,
) {
    // TODO - Create the main screen
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "MAIN SCREEN TEMP",
            color = MaterialTheme.colorScheme.onBackground,
        )

        Button(
            onClick = { goLogin() },
        ) {
            Text("LOGIN")
        }
    }
}