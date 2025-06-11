package dam.nathan

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import listenthis.composeapp.generated.resources.Res
import listenthis.composeapp.generated.resources.listenthis
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ListenThis",
        state = WindowState(width = 900.dp, height = 650.dp),
        icon = painterResource(Res.drawable.listenthis)
    ) {
        KoinApplication(application = {
            modules(appModule)
        }) {
            App()
        }
    }
}