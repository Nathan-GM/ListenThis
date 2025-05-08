package dam.nathan.repositories

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dam.nathan.classes.Connection
import dam.nathan.classes.PostsDatabase
import dam.nathan.repositories.abstracts.APostRepository
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId



class PostRepository(private val connection : Connection) : APostRepository() {

    //TODO Comment this class

    val env = dotenv {
        directory = "./"
        filename = "information.env"
    }

    val nameDB = env["DB_NAME"]
    val collection = env["POSTS_COLLECTION"]

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