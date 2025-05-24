package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


/**
 * Class that will save information related to a musical genre like their name and id.
 * This class will be used to send the information to the client.
 *
 * @param id Genre's id used to identify them internally in the database
 * @param name Genre's name
 *
 * @see PostsSerializables
 *
 * @author Nathan Gonzalez Mercado
 */
@Serializable
data class GenreSerializable(
    val id: String? = null,
    val name: String,
    val color: List<Int>
)

/**
 * Class that will save information related to a musical genre like their name and id.
 * This class will be used to send and get information from the database.
 *
 * @param id Genre's id used to identify them internally in the database
 * @param name Genre's name
 *
 * @see PostsDatabase
 *
 * @author Nathan Gonzalez Mercado
 */
data class GenreDatabase(
    @BsonId
    val id: ObjectId,
    val name: String,
    val color: List<Int>
)