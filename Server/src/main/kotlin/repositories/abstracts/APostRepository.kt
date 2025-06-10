package dam.nathan.repositories.abstracts

import dam.nathan.classes.GenreDatabase
import dam.nathan.classes.PostsDatabase
import dam.nathan.repositories.common.IRepository
import org.bson.types.ObjectId

/**
 * Abstract class related to the Posts. This adds 3 new functions
 *
 * @see PostsDatabase
 * @see IRepository
 */
abstract class APostRepository : IRepository <PostsDatabase, ObjectId> {
    /**
     * Function that will find the posts of a user
     * @param userID
     *
     * @return List of Posts, is there are not it will return an empty list.
     */
    abstract suspend fun findPostByUserId(userID: ObjectId): List<PostsDatabase>

    /**
     * Function that will add or remove a like  to a post
     *
     * @param post Post where the like will be added or removed.
     *
     * @see PostsDatabase
     *
     */
    abstract suspend fun addLike(post: PostsDatabase)

    /**
     * Function that will add a comment  to a post
     *
     * @param post Post where the comment will be added
     *
     * @see PostsDatabase
     *
     */
    abstract suspend fun addComment(post: PostsDatabase)
}