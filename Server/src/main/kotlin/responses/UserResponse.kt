package dam.nathan.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id:String?,
    val name:String,
)
