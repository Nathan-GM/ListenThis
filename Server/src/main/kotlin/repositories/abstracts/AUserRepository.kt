package dam.nathan.repositories.abstracts

import dam.nathan.classes.User
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

abstract class AUserRepository : IRepository<User, ObjectId> {
    abstract suspend fun getByUsername(username: String): User?
}