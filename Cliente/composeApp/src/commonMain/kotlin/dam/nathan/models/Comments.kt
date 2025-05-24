package dam.nathan.models

import kotlinx.serialization.Serializable

@Serializable
data class Comments(
    val authorId: String,
    val comment: String,
    val timeOfPost: Long
)
