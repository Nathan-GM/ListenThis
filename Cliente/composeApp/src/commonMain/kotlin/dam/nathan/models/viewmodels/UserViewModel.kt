package dam.nathan.models.viewmodels

import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import dam.nathan.models.User
import dam.nathan.models.UserwithToken
import dam.nathan.models.repositories.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking


class UserViewModel(val repository : UserRepository) : ViewModel() {
    private var _user = MutableStateFlow(UserwithToken())
    private var _token = MutableStateFlow(String())
    var user: StateFlow<UserwithToken> = _user


    fun getUser() : UserwithToken {
        return user.value
    }

    suspend fun setUser(user : User) {
        val databaseUser = repository.getUser(user)
        if (databaseUser != null) {
            val userWithToken = UserwithToken(
                user = databaseUser,
                token = _token.value
            )
            _user.value = userWithToken
        }
    }

    suspend fun getUserById(id: String) : User? {
        val databaseUser = repository.getUserById(id)
        if (databaseUser != null) return databaseUser else return null
    }

    suspend fun login(user: User) : String {
        var result = repository.login(user)
        if (result != "" && result != "timedout") {
            _token.value = result
            return "valid"
        } else if (result.equals("timedout")) {
            return "timeout"
        }
        else {
            return "error"
        }
    }

    fun logut() {
        _user.value.user = null
        _token.value = ""
    }

    suspend fun followGenre(genreId: String) : String {
        var originalUser = _user.value.user
        if (user.value.user!!.followedGenres!!.contains(genreId)) {
            _user.value.user!!.followedGenres!!.remove(genreId)
        } else {
            _user.value.user!!.followedGenres!!.add(genreId)
        }
        var result = repository.followGenre(genreId = genreId, user = _user.value.user!!, token = _user.value.token!!)
        if (result == "error") {
            _user.value.user = originalUser
            return "error"
        } else if (result == "timedout") {
            _user.value.user = originalUser
            return "timeout"
        } else {
            return "ok"
        }
    }

    suspend fun deleteAccount() : String {
        var originalUser = _user.value.user
        val result = repository.removeAccount(_user.value.user!!.id!!, _user.value.token!!)
        if (result == "error") {
            return "error"
        }
        else {
            logut()
            return "ok"
        }

    }

}