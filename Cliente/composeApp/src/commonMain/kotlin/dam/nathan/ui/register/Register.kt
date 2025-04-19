package dam.nathan.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    goLogin: () -> Unit,
) {
    // TODO - Create the register screen
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "REGISTER TEMP",
            color = MaterialTheme.colorScheme.onBackground,
        )
        Button(
            onClick = { goLogin() },
        ) {
            Text("LOGIN")
        }
    }
}