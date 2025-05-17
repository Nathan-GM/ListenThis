package dam.nathan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.runBlocking
import org.jetbrains.skia.Bitmap
import org.jetbrains.skiko.toBitmap
import org.jetbrains.skiko.toBufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

/**
 * Method specific for Desktop to load images on the application
 *
 */
@OptIn(ExperimentalEncodingApi::class)
@Composable
actual fun ib64(
    onChange: (String) -> Unit,
    initString: String?
) {
   var bitmap = remember { mutableStateOf<Bitmap?>(null) }
   var base64String = remember { mutableStateOf<String?>(null) }

   if (initString != null && base64String.value == null) {
       base64String.value = initString
       bitmap.value = ImageIO.read(ByteArrayInputStream(Base64.decode(base64String.value!!))).toBitmap()
   }

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Selecciona una imágen"
    ) { file ->

        run {
            runBlocking {
                val byteArray = file?.readBytes()
                if (byteArray != null) {
                    base64String.value = byteArray.let { Base64.encode(it) }
                    bitmap.value = ImageIO.read(ByteArrayInputStream(byteArray)).toBitmap()
                    onChange(base64String.value!!)
                }
            }
        }
    }

    Text(
        text = "Selecciona una imagen",
        style = MaterialTheme.typography.subtitle1, //Change it for a way smaller one
        modifier = Modifier.padding(bottom = 8.dp),
        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
    )

    Button(
        onClick = {
            runBlocking {
                launcher.launch()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Icon(
            imageVector = Icons.Filled.Photo,
            contentDescription = "image",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.background
        )
    }
    bitmap.value?.toBufferedImage()?.let {
        Image(
            painter = it.toPainter(),
            contentDescription = "Base 64 image",
            modifier = Modifier.size(500.dp)
        )
    }
}