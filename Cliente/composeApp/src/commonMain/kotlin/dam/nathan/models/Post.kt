package dam.nathan.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String? = null,
    val author: String,
    val media: String? = null,
    val ytURL : String? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val genre : String,
    val comments: MutableList<Comments>? = mutableListOf(),
    val likes: MutableList<Likes>? = mutableListOf(),
) {
    constructor(): this (
        "","","","","","",0L,""
    )
}