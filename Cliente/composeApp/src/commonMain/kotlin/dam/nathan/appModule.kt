package dam.nathan

import dam.nathan.models.repositories.GenreRepository
import dam.nathan.models.repositories.PostRepository
import dam.nathan.models.repositories.UserRepository
import dam.nathan.models.viewmodels.GenreViewModel
import dam.nathan.models.viewmodels.PostViewModel
import dam.nathan.models.viewmodels.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepository() }
    single<PostRepository> { PostRepository() }
    single<GenreRepository> { GenreRepository() }

    viewModel { UserViewModel(get()) }
    viewModel { PostViewModel(get()) }
    viewModel { GenreViewModel(get()) }
}