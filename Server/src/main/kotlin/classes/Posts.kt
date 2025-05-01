package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * Post Class that will be connecting to the client.
 *
 * @param id Post's ID. It will be a String of the ObjectID that belongs to the post
 * @param author ID of the user who made the post.
 * @param media Image or Video related to the publication
 * @param description Brief description of what's the post about
 * @param timestamp Moment where the post was created
 * @param genre ID of the genre the post is mentioning or is related.
 * @param comments List of comments that this publication will have
 * @param likes List of likes that the post got since it was posted.
 *
 * @author Nathan Gonzalez Mercado
 */

@Serializable
data class PostsSerializables(
    val id: String? = null,
    val author: String,
    val media : String? = null,
    val description : String,
    val timestamp : Long? = System.currentTimeMillis(),
    val genre : String,
    val comments: MutableList<CommentsSerializable>? = mutableListOf(),
    val likes : MutableList<LikesSerializable>? = mutableListOf()
)

/**
 * Post Class that will be connecting to the Database
 *
 * @param _id Post's ID. Will be used to identify the post in the database.
 * @param author ID of the person who posted the post.
 * @param media Image or Video in Base64 related to the post
 * @param timestamp Moment where the post was created
 * @param genre ID of the genre the post is related to
 * @param comments List of comments that the post have got since it got posted.
 * @param likes List of likes that the post have got since it got posted.
 *
 * @author Nathan Gonzalez Mercado
 */

data class PostsDatabase(
    @BsonId
    val _id : ObjectId,
    val author : ObjectId,
    val media: String? = null,
    val description : String,
    val timestamp : Long,
    val genre : ObjectId,
    val comments : MutableList<CommentsDatabase>,
    val likes : MutableList<LikesDatabase>,
)