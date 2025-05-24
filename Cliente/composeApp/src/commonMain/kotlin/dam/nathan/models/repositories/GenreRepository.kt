package dam.nathan.models.repositories

import dam.nathan.models.Genre
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException

class GenreRepository {

    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    val env = try {
        dotenv {
            directory = "/assets"
            filename = "information.env"
        }
    } catch (e: Exception) {
        dotenv {
            directory = "./.."
            filename = "information.env"
        }
    }

    val serverLocation = env["SERVER_IP"]
    val urlGenres = "${serverLocation}${env["GENRES_ROUTE"]}"

    suspend fun getGenres(): MutableList<Genre>? {
        try {
            val response: HttpResponse = client.get("$urlGenres") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<MutableList<Genre>>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }

    suspend fun getGenreById(id:String): Genre? {
        try {
            val response: HttpResponse = client.get("${urlGenres}id") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<Genre>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }

}