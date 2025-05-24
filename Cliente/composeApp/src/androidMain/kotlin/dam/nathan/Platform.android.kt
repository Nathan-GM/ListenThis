package dam.nathan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// TODO Do thhe actual function for android
@OptIn(ExperimentalEncodingApi::class)
@Composable
actual fun ib64(
    onChange: (String) -> Unit,
    initString: String?,
) {
    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var base64String = remember { mutableStateOf<String?>(null) }

    if (initString != null && base64String.value == null) {
        base64String.value = initString
        bitmap.value = BitmapFactory.decodeByteArray(
            Base64.decode(base64String.value!!),
            0,
            Base64.decode(base64String.value!!).size
        )
        //bitmap.value = ImageIO.read(ByteArrayInputStream(Base64.decode(base64String.value!!))).toBitmap()
    }

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecciona una imagen"
    ) { file ->
        run {
            runBlocking {
                val byteArray = file?.readBytes()
                if (byteArray != null) {
                    base64String.value = byteArray.let { Base64.encode(it) }

                    bitmap.value = BitmapFactory.decodeByteArray(
                        Base64.decode(base64String.value!!),
                        0,
                        Base64.decode(base64String.value!!).size
                    )

                    onChange(base64String.value!!)
                }
            }
        }
    }
    Button(
        onClick = {
            runBlocking {
                launcher.launch()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Image,
            contentDescription = ""
        )
    }

}

@Composable
actual fun darkMode(
    changeTheme: () -> Unit,
    isDarkModeOn: Boolean,
) {

}

@OptIn(ExperimentalEncodingApi::class)
@Composable
actual fun imageLoader(
    avatar: String,
    size: Int,
) {
    val bitmap = BitmapFactory.decodeByteArray(
        Base64.decode(avatar),
        0,
        Base64.decode(avatar).size
    )
    Image(
        bitmap.asImageBitmap(),
        contentDescription = "",
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .size(size.dp)
    )
}