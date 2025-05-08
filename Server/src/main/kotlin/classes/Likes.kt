package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


/**
 * Class that will save user ID of the person who liked a post
 * @param userId User's id who liked a post
 *
 * @see PostsSerializables
 *
 * @author Nathan Gonzalez Mercado
 */
@Serializable
data class LikesSerializable(
    val userId: String,
)

/**
 * Class that will save the users id in the database as an ObjectID.
 * @param _userId User's id who liked a post
 *
 * @see PostsDatabase
 *
 * @author Nathan Gonzalez Mercado
 */
data class LikesDatabase(
    @BsonId
    val _userId: ObjectId,
)
