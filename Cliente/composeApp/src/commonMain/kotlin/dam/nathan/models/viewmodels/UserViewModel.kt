package dam.nathan.models.viewmodels

import androidx.lifecycle.ViewModel
import dam.nathan.models.User
import dam.nathan.models.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class UserViewModel(val repository : UserRepository) : ViewModel() {
    private var _user = MutableStateFlow(User())
    var user : StateFlow<User> = _user

}