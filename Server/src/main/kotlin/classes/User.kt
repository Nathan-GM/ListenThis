package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * User Class that will be connecting to the database.
 *
 * @param id User's ID. It will be using the ObjectID.
 * @param username The username that the user will choose.
 * @param password User's password. Here it will appear encrypted.
 * @param biography User's biography. It will appear displayed on their page.
 * @param avatar User's avatar. It will be stored in Base64. Can be null
 *
 * @author Nathan Gonzalez Mercado
 *
 */
data class UserDatabase(
    @BsonId val id : ObjectId,
    val username : String,
    val password : String,
    val biography : String? = "",
    val avatar : String? = "",
)

/**
 * User Class that will be connecting to the client.
 *
 * @param id User's ID. It will be using string, being a String of the ObjectID.
 * @param username The username that the user will choose.
 * @param password User's password. Here it will appear encrypted.
 * @param biography User's biography. It will appear displayed on their page.
 * @param avatar User's avatar. It will be stored in Base64. Can be null
 *
 * @author Nathan Gonzalez Mercado
 *
 */
@Serializable
data class UserSerializable(
    val id : String? = "",
    val username : String,
    val password : String,
    val biography : String? = "",
    val avatar : String? = ""
)