package dam.nathan.classes


// TODO Add imports for the BSON ID and the Serializable

// User class that will be used to interact with MongoDB.
data class User {
    @BsonId val id : ObjectId,
    val username : String,
    val password : String,
    val biography : String,
    val avatar : String?,    
}

// User class that will be use to send the information to the client.
@Serializable
data class UserSerializable {
    val id : String?,
    val username : String,
    val password : String,
    val biography : String,
    val avatar : String?
}