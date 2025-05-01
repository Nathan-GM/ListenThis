package dam.nathan.classes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

//TODO Comment this two classes

@Serializable
data class GenreSerializable(
    val id: String? = null,
    val name: String
)

data class GenreDatabase(
    @BsonId
    val _id: ObjectId,
    val name: String
)