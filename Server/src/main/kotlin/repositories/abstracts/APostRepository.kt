package dam.nathan.repositories.abstracts

import dam.nathan.classes.PostsDatabase
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

abstract class APostRepository : IRepository <PostsDatabase, ObjectId> {
    abstract suspend fun findPostByUserId(userID: ObjectId): List<PostsDatabase>
    abstract suspend fun addLike(post: PostsDatabase)
    abstract suspend fun addComment(post: PostsDatabase)
}