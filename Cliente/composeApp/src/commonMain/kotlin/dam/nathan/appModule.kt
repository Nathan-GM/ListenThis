package dam.nathan

import dam.nathan.models.repositories.UserRepository
import dam.nathan.models.viewmodels.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepository() }

    viewModel { UserViewModel(get()) }
}