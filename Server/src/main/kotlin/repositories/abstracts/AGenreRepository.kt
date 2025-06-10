package dam.nathan.repositories.abstracts

import dam.nathan.classes.GenreDatabase
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

/**
 * Abstract class related to the Genre
 *
 * @see GenreDatabase
 * @see IRepository
 */
abstract class AGenreRepository : IRepository<GenreDatabase, ObjectId> {
}