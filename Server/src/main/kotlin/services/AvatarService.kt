package dam.nathan.services

import io.github.cdimascio.dotenv.dotenv
import org.bson.types.ObjectId
import java.io.File
import kotlin.math.abs

val env = dotenv{
    directory = "./"
    filename = "information.env"
}

val avatarDirectoryRoute = env["AVATAR_DIRECTORY"]

//This 2 are used do a route error where it will take the whole route as needed but added a dot in random spots.
val tmp = File(avatarDirectoryRoute)
val absoluteRoute = tmp.absolutePath.replace(".", "")

val avatarDirectory = File(absoluteRoute)

fun saveAvatar(avatar: String, id: ObjectId) : String {
    val fileName = "${id.toString()}.txt"
    var fileExists = false

    for (file in avatarDirectory.listFiles()) {
        if (file.name == fileName) {
            fileExists = true
            break
        }
    }

    val avatarFile = File("$avatarDirectory/$fileName")
    if (!fileExists) {
        avatarFile.createNewFile()
    }
    avatarFile.writeText(avatar)
    return avatarFile.name
}

fun loadAvatar(fileName: String): String {
    val files = avatarDirectory.listFiles()
    for (file in files) {
        if (file.name == fileName) {
            return file.readText()
        }
    }
    return ""
}