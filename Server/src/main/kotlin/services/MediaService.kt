package dam.nathan.services

import io.github.cdimascio.dotenv.dotenv
import org.bson.types.ObjectId
import java.io.File

/**
 * TODO Do this service
 * Might be better do them when the client is able to create post so it is easy to test them.
 */

val envMedia = dotenv {
    directory = "./"
    filename = "information.env"
}

val postDirectoryRoute = env["POST_MEDIA_DIRECTORY"]

//This 2 are used do a route error where it will take the whole route as needed but added a dot in random spots.
val tmpMedia = File(postDirectoryRoute)
val absoluteRouteMedia = tmpMedia.absolutePath.replace(".", "")


val postDirectory = File(absoluteRouteMedia)

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

fun loadImageForPost(imageRoute : String): String {
    val files = postDirectory.listFiles()
    for (file in files) {
        if (file.name == imageRoute) {
            return file.readText()
        }
    }
    return ""
}