package dam.nathan

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// TODO Do thhe actual function for android
actual fun ib64(
    onChange: (String) -> Unit,
    initString: String?=null
) {

}