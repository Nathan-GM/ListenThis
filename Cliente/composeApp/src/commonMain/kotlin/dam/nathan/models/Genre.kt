package dam.nathan.models

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: String? = null,
    val name: String,
    val color: List<Int>
) {
    constructor(): this(
        "", "", emptyList<Int>()
    )
}
