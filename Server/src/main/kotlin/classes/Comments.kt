package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


/**
 * Class that will save information related to a comment, such as the user who posted it or when they did that.
 * This specific one will be used for the communication between the client and the server
 *
 * @param authorId User's id of the person who posted the comment.
 * @param comment Actual comment on a specific post.
 * @param timeOfPost Moment when the post was made. In case there's no time indicated the server will assign the moment when it gets the object.
 *
 * @see PostsSerializables
 *
 * @author Nathan Gonzalez Mercado
 */

@Serializable
data class CommentsSerializable(
    val authorId: String? = null,
    val comment: String,
    val timeOfPost : Long? = System.currentTimeMillis(),
)

/**
 * Class that will save information related to a comment, such as the user who posted it or when they did that.
 * This specific one will be used for the communication between the server and the database
 *
 * @param authorId User's id of the person who posted the comment.
 * @param comment Actual comment on a specific post.
 * @param timeOfPost Moment when the post was made.
 *
 * @see PostsDatabase
 *
 * @author Nathan Gonzalez Mercado
 */
data class CommentsDatabase(
    @BsonId
    val _authorId : ObjectId,
    val comment : String,
    val timeOfPost : Long,
)
