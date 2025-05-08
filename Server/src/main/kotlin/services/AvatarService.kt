package dam.nathan.services

import io.github.cdimascio.dotenv.dotenv
import org.bson.types.ObjectId
import java.io.File

val env = dotenv{
    directory = "./"
    filename = "information.env"
}

val avatarDirectoryRoute = env["AVATAR_DIRECTORY"]
val avatarDirectory = File(avatarDirectoryRoute)

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