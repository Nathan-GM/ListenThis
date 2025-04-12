package dam.nathan.repositories

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dam.nathan.classes.Connection
import dam.nathan.classes.UserDatabase
import dam.nathan.repositories.abstracts.AUserRepository
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

/**
 * Class that contains information, providing features like adding users, removing them and updating on the database.
 *
 * @param connection Connection of the database
 *
 * @see AUserRepository
 * @see Connection
 *
 * @author Nathan Gonzalez Mercado
 */
class UserRepository(private val connection:Connection) : AUserRepository() {

    //Env variable that will get data from the environment file.
    val env = dotenv{
        directory = "./"
        filename = "information.env"
    }

    //Variables that contains the name of the database and the collection
    val nameDB = env["DB_NAME"]
    val collection = env["USER_COLLECTION"]

    /**
     * Function that will get information about a user by using their username.
     *
     * @param username User that will be looking form
     * @return Returns a UserDatabase if one is found or null if there's no user with that username
     * @see UserDatabase
     * @see Connection
     */
    override suspend fun getByUsername(username: String): UserDatabase? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("username", username)
            val user = collectionDB.find(query)
            return user.firstOrNull()
        }
        return null
    }

    /**
     * Function that will get all the users stored in the database
     *
     * @return List of UserDatabase that are found on the database. If no user is found, an empty list will be returned.
     * @see UserDatabase
     * @see Connection
     */
    override suspend fun getAll(): List<UserDatabase> {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val users = collectionDB.find()
            return users.toList()
        }
        return emptyList()
    }

    /**
     * Function that will return a user based on an ID
     *
     * @param id An objectID that can or not the one a user stored in the database
     * @return It will return a user if one is found or null if no user is found.
     * @see UserDatabase
     * @see Connection
     */
    override suspend fun getById(id: ObjectId): UserDatabase? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("id", id)
            val user = collectionDB.find(query)
            return user.firstOrNull()
        }
        return null
    }

    /**
     * Function that will update a user information. It will update the user by using it's ID.
     *
     * @param item User that will be getting the update
     * @see Connection
     * @see UserDatabase
     *
     */
    override suspend fun update(item: UserDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("id", item.id)
            var updates = Updates.combine(
                Updates.set("username", item.username),
                Updates.set("password", item.password),
                Updates.set("biography", item.biography),
                Updates.set("avatar", item.avatar)
            )
            collectionDB.findOneAndUpdate(query, updates)
        }
    }
    /**
     * Function that will update a user information.
     *
     * @param item User that will be getting the update
     * @param id ID of the user that will be getting the update.
     * @see Connection
     * @see UserDatabase
     *
     */
    override suspend fun updatebyId(item: UserDatabase, id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("id", id)
            var updates = Updates.combine(
                Updates.set("username", item.username),
                Updates.set("password", item.password),
                Updates.set("biography", item.biography),
                Updates.set("avatar", item.avatar)
            )
            collectionDB.findOneAndUpdate(query, updates)
        }
    }

    /**
     * Function that will remove a user from the database
     *
     * @param item User that will be removed.
     * @see Connection
     * @see UserDatabase
     */
    override suspend fun remove(item: UserDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("id", item.id)
            collectionDB.deleteOne(query)
        }
    }

    /**
     * Function that will remove a user, based on an ID, from the database.
     *
     * @param id ID from the user that will be removed.
     *
     * @see Connection
     */
    override suspend fun removeById(id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val query = Filters.eq("id", id)
            collectionDB.deleteOne(query)
        }
    }

    /**
     * Function that will add a new user to the database
     *
     * @param item User that will be added to the database
     *
     * @return This will return a String, where the ID of the user will be stored.
     *
     * @see UserDatabase
     * @see Connection
     */
    override suspend fun add(item: UserDatabase) : String? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<UserDatabase>(collection)
            val result = collectionDB.insertOne(item)
            return result.insertedId?.toString()
        }
        return null
    }

}