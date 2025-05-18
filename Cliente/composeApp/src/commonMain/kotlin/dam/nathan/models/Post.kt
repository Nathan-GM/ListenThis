package dam.nathan.models

data class Post(
    val id: String? = null,
    val author: String,
    val media: String? = null,
    val content: String,
    val timestamp: Long,
    val genre : String,
    val comments: MutableList<Comments>, // TMP
    val likes: MutableList<Likes>, // TMP
)