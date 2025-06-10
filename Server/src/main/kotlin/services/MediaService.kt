package dam.nathan.services

import org.bson.types.ObjectId
import java.io.File


val postDirectoryRoute = env["POST_MEDIA_DIRECTORY"]

//This 2 are used do a route error where it will take the whole route as needed but added a dot in random spots.
val tmpMedia = File(postDirectoryRoute)
val absoluteRouteMedia = tmpMedia.absolutePath.replace(".", "")


val postDirectory = File(absoluteRouteMedia)

/**
 * Function that will save a picture with the postId as the file name.
 *
 * @param image Base64 that contains the image.
 * @param postId Post's id.
 *
 * @return The file name where the image's stored.
 */
fun saveImageForPost(image: String, postId: ObjectId) : String {
    val fileName = "${postId.toString()}.txt"
    var fileExists = false

    for (file in postDirectory.listFiles()) {
        if (file.name == fileName) {
            fileExists = true
            break
        }
    }

    val imageFile = File("$postDirectory/$fileName")
    if (!fileExists) {
        imageFile.createNewFile()
    }
    imageFile.writeText(image)
    return imageFile.name
}

/**
 * Function that will load an image.
 *
 * @param imageRoute File where the image's stored.
 *
 * @return The text saved on the file, it will be the image encoded in Base64.
 */
fun loadImageForPost(imageRoute : String): String {
    val files = postDirectory.listFiles()
    for (file in files) {
        if (file.name == imageRoute) {
            return file.readText()
        }
    }
    return ""
}
/**
 * Function that will remove an image.
 *
 * @param imageName File where the image's stored.
 */
fun removeImageForPost(imageName: String) {
    for (file in postDirectory.listFiles()) {
        if (file.name == imageName) {
            file.delete()
            break
        }
    }
}