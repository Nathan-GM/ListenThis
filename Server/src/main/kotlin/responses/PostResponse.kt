package dam.nathan.responses

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: String?,
    val author: String,
    val media: String?,
    val title: String,
    val content : String,
    val timestamp : Long,
)
