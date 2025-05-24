package dam.nathan.models.viewmodels

import androidx.lifecycle.ViewModel
import dam.nathan.models.Genre
import dam.nathan.models.repositories.GenreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GenreViewModel(val repository: GenreRepository) : ViewModel() {
    private var _genres = MutableStateFlow<MutableList<Genre>>(mutableListOf())
    var genres: StateFlow<MutableList<Genre>> = _genres

    suspend fun getAllGenres(): MutableList<Genre>? {
        val genresTMP = repository.getGenres()

        if (genresTMP == null) {
            return null
        } else {
            if (genresTMP.isEmpty()) return mutableListOf<Genre>()
            _genres.value = genresTMP
            return genres.value
        }
    }

    suspend fun getById(id: String) : Genre? {
        val tmp = repository.getGenreById(id)
        if (tmp == null) {
            return null
        } else {
            return tmp
        }
    }
}