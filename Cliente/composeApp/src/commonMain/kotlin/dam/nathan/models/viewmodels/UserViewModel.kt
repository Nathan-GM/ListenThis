package dam.nathan.models.viewmodels

import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import dam.nathan.models.User
import dam.nathan.models.UserwithToken
import dam.nathan.models.repositories.UserRepository
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

    fun setUser(user : User) {
        println("iniciando set user")
        val databaseUser = runBlocking {
            repository.getUser(user)
        }
        if (databaseUser != null) {
            val userWithToken = UserwithToken(
                user = databaseUser,
                token = _token.value
            )
            _user.value = userWithToken
        }
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

}