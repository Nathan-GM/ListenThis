package dam.nathan.repositories.abstracts

import dam.nathan.classes.UserDatabase
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

/**
 * Abstract implementation of the basic interface.
 *
 * @see IRepository
 */
abstract class AUserRepository : IRepository<UserDatabase, ObjectId> {
    abstract suspend fun getByUsername(username: String): UserDatabase?
}