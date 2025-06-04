package dam.nathan

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.koin.compose.KoinApplication

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ListenThis",
        state = WindowState(width = 900.dp, height = 650.dp),
    ) {
        KoinApplication(application = {
            modules(appModule)
        }) {
            App()
        }
    }
}