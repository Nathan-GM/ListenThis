package dam.nathan.models.viewmodels

import androidx.lifecycle.ViewModel
import dam.nathan.models.Post
import dam.nathan.models.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

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
}