package dam.nathan.services

import dam.nathan.classes.UserDatabase
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
val defaulAvatars : Array<String> = arrayOf("0_1.txt", "0_2.txt", "0_3.txt", "0_4.txt")


/**
 * Function that will save an avatar with the userID as the file name.
 *
 * @param avatar Base64 that contains the avatar.
 * @param id User's id.
 *
 * @return The file name where the avatar's stored.
 */
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

/**
 * Function that will load an avatar.
 *
 * @param fileName File where the avatar's stored.
 *
 * @return The text saved on the file, it will be the avatar encoded in Base64.
 */
fun loadAvatar(fileName: String): String {
    val files = avatarDirectory.listFiles()
    for (file in files) {
        if (file.name == fileName) {
            return file.readText()
        }
    }
    return ""
}

/**
 * Function that will delete an avatar.
 *
 * @param fileName File where the avatar's stored.
 *
 */
fun removeAvatar(fileName: String) {
    if (defaulAvatars.contains(fileName)) {
        return
    }
    for (avatarFile in avatarDirectory.listFiles()) {
        if (avatarFile.name == fileName) {
            avatarFile.delete()
        }
    }
}