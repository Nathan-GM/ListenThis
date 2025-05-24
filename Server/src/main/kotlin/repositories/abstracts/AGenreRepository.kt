package dam.nathan.repositories.abstracts

import dam.nathan.classes.GenreDatabase
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

abstract class AGenreRepository : IRepository<GenreDatabase, ObjectId> {
}