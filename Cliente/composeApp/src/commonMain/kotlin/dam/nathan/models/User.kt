package dam.nathan.models

import kotlinx.serialization.Serializable

/**
 * Class related to users. It contains the username, the cypher password,
 * the avatar the user has and the biography
 *
 * @param id User's id. Will be used to identify the user in the different actions a user can make in the application
 * @param username Name that will be displayed to everyone in the application.
 * @param password User's password. Mainly used in the login to confirm the account exists.
 * @param avatar User's avatar. Stored in Base64 and will be displayed after decoding. Can be null and in case it is, a default icon will be loaded.
 * @param biography User's biography. Will be displayed on their profile and will contain a small text about them. It can be empty.
 *
 * @constructor Blank constructor in case once it's ever needed.
 *
 * @author Nathan Gonzalez Mercado
 */
@Serializable
data class User(
    val id: String? = null,
    val username: String,
    val password: String,
    val avatar: String? = "",
    val biography: String? = "",
    val followedGenres: MutableList<String>? = mutableListOf()
) {
    constructor(): this(
        "", "", "", "", ""
    )
}


/**
 * Class that will contain the user with its token. Will mainly be used for the interactions with the application
 *
 * @param user User information.
 * @param token User tokens. Will be used to validate the user through the application
 *
 * @constructor Blank constructor in case it's ever needed.
 *
 * @see User
 *
 * @author Nathan Gonzalez Mercado
 */
data class UserwithToken(
    var user : User? = null,
    val token: String? = ""
)