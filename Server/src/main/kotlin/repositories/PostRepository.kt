package dam.nathan.repositories

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dam.nathan.classes.Connection
import dam.nathan.classes.PostsDatabase
import dam.nathan.classes.UserDatabase
import dam.nathan.repositories.abstracts.APostRepository
import dam.nathan.repositories.abstracts.AUserRepository
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId


/**
 * Class that contains information, providing features like adding posts, removing them and updating on the database.
 *
 * @param connection Connection of the database
 *
 * @see APostRepository
 * @see Connection
 *
 * @author Nathan Gonzalez Mercado
 */
class PostRepository(private val connection : Connection) : APostRepository() {

    val env = dotenv {
        directory = "./"
        filename = "information.env"
    }

    val nameDB = env["DB_NAME"]
    val collection = env["POSTS_COLLECTION"]

    /**
     * Function that will get all the posts stored in the database
     *
     * @return List of PostDatabase that are found on the database. If no posts are found, an empty list will be returned.
     * @see PostsDatabase
     * @see Connection
     */
    override suspend fun getAll(): List<PostsDatabase> {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val posts = collection.find()
            return posts.toList()
        }
        return emptyList()
    }

    /**
     * Function that will return a post based on an ID
     *
     * @param id An objectID that is related to a post.
     * @return It will return a post if one is found or null if no post is found.
     * @see PostsDatabase
     * @see Connection
     */
    override suspend fun getById(id: ObjectId): PostsDatabase? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", id)
            val post = collection.find(query)
            return post.toList().firstOrNull()
        }
        return null
    }

    /**
     * Function that will update a post information. It will update the post by using it's ID.
     *
     * @param item Post that will be getting the update
     * @see Connection
     * @see PostsDatabase
     *
     */
    override suspend fun update(item: PostsDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", item._id)
            // Others things about the post shouldn't be changed
            var updated = Updates.combine(
                Updates.set("content", item.content),
                Updates.set("comments", item.comments),
                Updates.set("likes", item.likes),
            )
            collection.findOneAndUpdate(query, updated)
        }
    }
    /**
     * Function that will update a post information. It will update the post by using it's ID.
     *
     * @param item Post that will be getting the update
     * @see Connection
     * @see PostsDatabase
     *
     */
    override suspend fun updatebyId(item: PostsDatabase, id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", id)
            // Others things about the post shouldn't be changed
            var updated = Updates.combine(
                Updates.set("content", item.content),
                Updates.set("comments", item.comments),
                Updates.set("likes", item.likes),
            )
            collection.findOneAndUpdate(query, updated)
        }
    }
    /**
     * Function that will remove a post
     *
     * @param item Post that will be removed.
     * @see Connection
     * @see PostsDatabase
     *
     */
    override suspend fun remove(item: PostsDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", item._id)
            collection.deleteOne(query)
        }
    }

    /**
     * Function that will remove a post by id
     *
     * @param id id of the post that will be removed.
     * @see Connection
     * @see PostsDatabase
     *
     */
    override suspend fun removeById(id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", id)
            collection.deleteOne(query)
        }
    }

    /**
     * Function that will add a new post.
     *
     * @param item Post that will be added to the database
     * @see Connection
     * @see PostsDatabase
     *
     * @return id of the post if the post is added or null if it returns an error
     *
     */
    override suspend fun add(item: PostsDatabase): String? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val result = collection.insertOne(item)
            return result.insertedId?.toString()
        }
        return null
    }

    /**
     * @see APostRepository.findPostByUserId
     */
    override suspend fun findPostByUserId(userID: ObjectId): List<PostsDatabase> {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("author", userID)
            val result = collection.find(query)
            return result.toList()
        }
        return emptyList()
    }
    /**
     * @see APostRepository.addLike
     */
    override suspend fun addLike(post: PostsDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", post._id)
            val update = Updates.combine(
                Updates.set("likes", post.likes)
            )
            collection.findOneAndUpdate(query, update)

        }
    }
    /**
     * @see APostRepository.addComment
     */
    override suspend fun addComment(post: PostsDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val database = connection.getDataBase(nameDB)
        database?.let {
            val collection = it.getCollection<PostsDatabase>(collection)
            val query = Filters.eq("_id", post._id)
            val update = Updates.combine(
                Updates.set("comments", post.comments)
            )
            collection.findOneAndUpdate(query, update)
        }
    }

}