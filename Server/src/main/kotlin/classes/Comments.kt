package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

//TODO Comment this two classes

@Serializable
data class CommentsSerializable(
    val id: String? = null,
    val comment: String,
    val timeOfPost : Long? = System.currentTimeMillis(),
)

data class CommentsDatabase(
    @BsonId
    val _id : ObjectId,
    val comment : String,
    val timeOfPost : Long,
)
