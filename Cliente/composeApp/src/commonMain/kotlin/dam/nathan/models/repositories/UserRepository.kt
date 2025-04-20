package dam.nathan.models.repositories

import dam.nathan.models.User
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.gson.*


class UserRepository {
    /* Client that will connect to the KTOR server */
    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
    }

    val urlUsers = "http://192.168.18.151:8090/users/"

    suspend fun register(u: User) : HttpResponse {
        val response : HttpResponse = client.post(urlUsers) {
            contentType(ContentType.Application.Json)
            setBody(
                u
            )
        }
        return response
    }
}