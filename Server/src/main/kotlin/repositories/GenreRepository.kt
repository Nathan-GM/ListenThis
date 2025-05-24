package dam.nathan.repositories

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import dam.nathan.classes.Connection
import dam.nathan.classes.GenreDatabase
import dam.nathan.repositories.abstracts.AGenreRepository
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class GenreRepository(private val connection: Connection) : AGenreRepository() {

    val env = dotenv {
        directory = "./"
        filename = "information.env"
    }

    val nameDB = env["DB_NAME"]
    val collection = env["GENRES_COLLECTION"]

    override suspend fun getAll(): List<GenreDatabase> {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val genres = collectionDB.find()
            return genres.toList()
        }
        return emptyList()
    }

    override suspend fun getById(id: ObjectId): GenreDatabase? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val query = Filters.eq("_id", id)
            val genre = collectionDB.find(query)
            return genre.firstOrNull()
        }
        return null
    }

    override suspend fun update(item: GenreDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val query = Filters.eq("_id", item.id)
            val updates = Updates.set("name", item.name)
            collectionDB.findOneAndUpdate(query, updates)
        }
    }

    override suspend fun updatebyId(item: GenreDatabase, id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val query = Filters.eq("_id", id)
            val updates = Updates.set("name", item.name)
            collectionDB.findOneAndUpdate(query, updates)
        }
    }

    override suspend fun remove(item: GenreDatabase) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val query = Filters.eq("_id", item.id)
            collectionDB.deleteOne(query)
        }
    }

    override suspend fun removeById(id: ObjectId) {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val query = Filters.eq("_id", id)
            collectionDB.deleteOne(query)
        }
    }

    override suspend fun add(item: GenreDatabase): String? {
        if (!connection.isOpen()) {
            connection.connect()
        }
        val db = connection.getDataBase(nameDB)
        db?.let {
            val collectionDB = it.getCollection<GenreDatabase>(collection)
            val result = collectionDB.insertOne(item)
            return result.insertedId?.toString()
        }
        return null
    }

}