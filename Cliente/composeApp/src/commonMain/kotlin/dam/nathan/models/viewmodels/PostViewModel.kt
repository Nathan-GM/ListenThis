package dam.nathan.models.viewmodels

import androidx.lifecycle.ViewModel
import dam.nathan.models.Post
import dam.nathan.models.User
import dam.nathan.models.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PostViewModel(val repository : PostRepository) : ViewModel() {
    private var _posts = MutableStateFlow<MutableList<Post>>(mutableListOf())
    var posts : StateFlow<MutableList<Post>> = _posts

    suspend fun getAllPosts() : MutableList<Post>? {
        val postTMP = repository.getPosts()
        if (postTMP == null) {
            return null
        } else {
            if (postTMP.isEmpty()) return mutableListOf()
            _posts.value = postTMP
            return posts.value
        }
    }

    suspend fun addPost(post: Post, token:String) : String {
        val result = repository.createPost(post, token)
        if (result != null) {
            _posts.value.add(result)
            return "volver"
        } else {
            return "error"
        }
    }

    suspend fun getUserPosts(user: User) : MutableList<Post> {
        if (_posts.value.isEmpty()) {
            getAllPosts()
        }
        var userPosts = _posts.value.filter { x -> x.author == user.id }.toMutableList()
        if (userPosts.isEmpty()) {
            return mutableListOf()
        } else {
            return userPosts
        }
    }

    suspend fun updatePost(post: Post, token: String) : String {
        val result = repository.updatePost(post, token)
        if (result != null && result == "ok") {
            updatePostsList(post)
            return "ok"
        } else {
            return "error"
        }
    }


    private fun updatePostsList(update: Post) {
        _posts.value = _posts.value.map { post ->
            if (post.id == update.id) update else post
        }.toMutableList()
    }
}