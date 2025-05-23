package dam.nathan

import android.os.Build
import androidx.compose.runtime.Composable

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// TODO Do thhe actual function for android
@Composable
actual fun ib64(
    onChange: (String) -> Unit,
    initString: String?
) {

}

@Composable
actual fun darkMode(
    changeTheme: () -> Unit,
    isDarkModeOn: Boolean
) {

}